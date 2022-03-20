/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.sw.marshall;

import org.kie.workbench.common.stunner.core.api.FactoryManager;
import org.kie.workbench.common.stunner.core.command.CommandResult;
import org.kie.workbench.common.stunner.core.command.impl.CompositeCommand;
import org.kie.workbench.common.stunner.core.factory.graph.EdgeFactory;
import org.kie.workbench.common.stunner.core.factory.graph.NodeFactory;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandExecutionContext;
import org.kie.workbench.common.stunner.core.graph.command.impl.AddChildNodeCommand;
import org.kie.workbench.common.stunner.core.graph.command.impl.AddConnectorCommand;
import org.kie.workbench.common.stunner.core.graph.command.impl.AddNodeCommand;
import org.kie.workbench.common.stunner.core.graph.command.impl.SetConnectionTargetNodeCommand;
import org.kie.workbench.common.stunner.core.graph.content.Bound;
import org.kie.workbench.common.stunner.core.graph.content.Bounds;
import org.kie.workbench.common.stunner.core.graph.content.view.MagnetConnection;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.impl.EdgeImpl;
import org.kie.workbench.common.stunner.core.rule.RuleViolation;
import org.kie.workbench.common.stunner.sw.definition.CallFunctionAction;
import org.kie.workbench.common.stunner.sw.definition.CallSubflowAction;
import org.kie.workbench.common.stunner.sw.definition.End;
import org.kie.workbench.common.stunner.sw.definition.EventRef;
import org.kie.workbench.common.stunner.sw.definition.EventState;
import org.kie.workbench.common.stunner.sw.definition.InjectState;
import org.kie.workbench.common.stunner.sw.definition.OnEvent;
import org.kie.workbench.common.stunner.sw.definition.Start;
import org.kie.workbench.common.stunner.sw.definition.SwitchState;

public class BuilderContext {

    private final Context context;
    private final FactoryManager factoryManager;

    Node parentNode;
    Node sourceNode;
    private CompositeCommand.Builder storageCommands;
    private CompositeCommand.Builder connectionCommands;

    public BuilderContext(Context context, FactoryManager factoryManager) {
        this.context = context;
        this.factoryManager = factoryManager;
        this.parentNode = null;
        this.sourceNode = null;
        this.storageCommands = new CompositeCommand.Builder();
        this.connectionCommands = new CompositeCommand.Builder();
    }

    public String obtainUUID(String name) {
        return context.obtainUUID(name);
    }

    @SuppressWarnings("all")
    public Node addNode(String name,
                        Object bean) {
        final String uuid = context.obtainUUID(name);
        final Node node = (Node) factoryManager.registry().getElementFactory(NodeFactory.class).build(uuid, bean);
        updateNodeBounds(node);
        if (null == parentNode && null == context.getWorkflowRootNode()) {
            storageCommands.addCommand(new AddNodeCommand(node));
        } else if (null == parentNode) {
            storageCommands.addCommand(new AddChildNodeCommand(context.getWorkflowRootNode(), node, null));
        } else {
            storageCommands.addCommand(new AddChildNodeCommand(parentNode, node, null));
        }
        return node;
    }

    @SuppressWarnings("all")
    public Edge addEdge(String uuid, Object bean, Node source) {
        final EdgeImpl edge = (EdgeImpl) factoryManager.registry().getElementFactory(EdgeFactory.class).build(uuid, bean);
        storageCommands.addCommand(new AddConnectorCommand(source, edge, MagnetConnection.Builder.atCenter(source)));
        return edge;
    }

    @SuppressWarnings("all")
    public void connect(Edge edge, Node source, String targetUUID) {
        connectionCommands.addCommand(new SetConnectionTargetNodeCommand(targetUUID,
                                                                         edge.getUUID(),
                                                                         MagnetConnection.Builder.at(0, 0)) {
            public CommandResult<RuleViolation> execute(GraphCommandExecutionContext context) {
                final Node<? extends View<?>, Edge> targetNode = getTargetNode(context);
                if (null != targetNode) {
                    asMagnetConnection().setIndex(0);
                    asMagnetConnection().setAuto(false);
                    // asMagnetConnection().setLocation(MagnetConnection.Builder.forTarget(source, targetNode).getLocation().copy());
                }
                return super.execute(context);
            }

            private MagnetConnection asMagnetConnection() {
                return (MagnetConnection) getConnection();
            }
        });
    }

    @SuppressWarnings("all")
    public Edge addEdgeToTargetName(Object transition,
                                    Node source,
                                    String target) {
        final String targetUUID = context.obtainUUID(target);
        return addEdgeToTargetUUID(transition, source, targetUUID);
    }

    @SuppressWarnings("all")
    public Edge addEdgeToTargetUUID(Object transition,
                                    Node source,
                                    String targetUUID) {
        final String transitionUUID = Context.generateUUID();
        final Edge tEdge = addEdge(transitionUUID, transition, source);
        connect(tEdge, source, targetUUID);
        return tEdge;
    }

    public CompositeCommand<GraphCommandExecutionContext, RuleViolation> commands() {
        return new CompositeCommand.Builder<>()
                .addCommand(storageCommands.build())
                .addCommand(connectionCommands.build())
                .build();
    }

    Context getContext() {
        return context;
    }

    private void updateNodeBounds(Node<View, Edge> node) {
        final Bounds bounds = node.getContent().getBounds();
        final Bound upperLeft = bounds.getUpperLeft();
        final double[] beanSize = getBeanSize(node.getContent().getDefinition());
        bounds.setLowerRight(new Bound(upperLeft.getX() + beanSize[0], upperLeft.getY() + beanSize[1]));
    }


    // TODO: Those size are just being "hardcoded" and matching actual svg declarations, for now.
    private static double[] getBeanSize(Object bean) {
        Class<?> type = bean.getClass();
        if (Start.class.equals(type)) {
            return new double[]{20d, 20d};
        } else if (End.class.equals(type)) {
            return new double[]{20d, 20d};
        } else if (EventRef.class.equals(type)) {
            return new double[]{56d, 56d};
        } else if (EventState.class.equals(type)) {
            return new double[]{154d, 102d};
        } else if (InjectState.class.equals(type)) {
            return new double[]{154d, 102d};
        } else if (SwitchState.class.equals(type)) {
            return new double[]{154d, 102d};
        } else if (OnEvent.class.equals(type)) {
            return new double[]{56d, 56d};
        } else if (CallFunctionAction.class.equals(type)) {
            return new double[]{154d, 51d};
        } else if (CallSubflowAction.class.equals(type)) {
            return new double[]{154d, 51d};
        }
        return new double[]{0d, 0d};
    }

}
