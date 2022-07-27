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

package com.ait.lienzo.client.core.shape.wires;

import com.ait.lienzo.client.core.types.BoundingBox;

public class WiresAutoMagnetSelector {

    private static int MINX = 0;
    private static int MINY = 1;
    private static int MAXX = 2;
    private static int MAXY = 3;

    private static double MAGNET_BOX_RATIO = 1;

    private static double getMaxY(double[] bb) {
        return bb[MAXY] * MAGNET_BOX_RATIO;
    }

    private static double getMaxX(double[] bb) {
        return bb[MAXX] * MAGNET_BOX_RATIO;
    }

    private static double getMinY(double[] bb) {
        return bb[MINY] / MAGNET_BOX_RATIO;
    }

    private static double getMinX(double[] bb) {
        return bb[MINX] / MAGNET_BOX_RATIO;
    }

    public static int getHeadMagnetNonOverlappedShapes(BoundingBox bb, BoundingBox compared) {
        double[] bbs = bbToDoubles(bb);
        double[] compareds = bbToDoubles(compared);
        return getHeadMagnetNonOverlappedShapes(bbs, compareds);
    }

    public static int getHeadMagnetNonOverlappedShapes(double[] bb, double[] compared) {
        boolean headAbove = getMaxY(bb) < compared[MINY];
        boolean headBelow = getMinY(bb) > compared[MAXY];
        boolean headLeft = getMaxX(bb) < compared[MINX];
        boolean headRight = getMinX(bb) > compared[MAXX];

        int magnet = -1;
        if (headAbove) {
            if (false && headLeft) {
                magnet = 3;
            } else if (false && headRight) {
                magnet = 6;
            } else {
                magnet = 5;
            }
        } else if (headBelow) {
            if (false && headLeft) {
                magnet = 2;
            } else if (false && headRight) {
                magnet = 8;
            } else {
                magnet = 1;
            }
        } else {
            if (headLeft) {
                magnet = 3;
            } else if (headRight) {
                magnet = 7;
            }
        }

        return magnet;
    }

    public static int getTailMagnetNonOverlappedShapes(BoundingBox bb, BoundingBox compared) {
        double[] bbs = bbToDoubles(bb);
        double[] compareds = bbToDoubles(compared);
        return getTailMagnetNonOverlappedShapes(bbs, compareds);
    }

    public static int getTailMagnetNonOverlappedShapes(double[] bb, double[] compared) {
        boolean headAbove = getMaxY(bb) < compared[MINY];
        boolean headBelow = getMinY(bb) > compared[MAXY];
        boolean headLeft = getMaxX(bb) < compared[MINX];
        boolean headRight = getMinX(bb) > compared[MAXX];

        int magnet = -1;
        if (headAbove) {
            if (headLeft) {
                magnet = 3;
            } else if (headRight) {
                magnet = 7;
            } else {
                magnet = 5;
            }
        } else if (headBelow) {
            if (headLeft) {
                magnet = 3;
            } else if (headRight) {
                magnet = 7;
            } else {
                magnet = 1;
            }
        } else {
            if (headLeft) {
                magnet = 3;
            } else if (headRight) {
                magnet = 7;
            }
        }

        return magnet;
    }

    private static double[] bbToDoubles(BoundingBox bb) {
        return new double[] { bb.getMinX(), bb.getMinY(), bb.getMaxX(), bb.getMaxY() };
    }

}
