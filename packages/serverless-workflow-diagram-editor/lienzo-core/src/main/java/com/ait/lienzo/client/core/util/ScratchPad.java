/*
   Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.util;

import com.ait.lienzo.client.core.config.LienzoCore;
import com.ait.lienzo.client.core.types.ImageDataPixelColor;
import com.ait.lienzo.client.core.types.PathPartEntryJSO;
import com.ait.lienzo.client.core.types.PathPartListJSO;
import com.ait.lienzo.shared.core.types.DataURLType;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.OffscreenCanvas;
import elemental2.dom.OffscreenCanvasRenderingContext2D;
import elemental2.dom.URL;
import jsinterop.base.Js;

public final class ScratchPad {

    private int m_wide;

    private int m_high;

    //handrey
    //private final HTMLCanvasElement m_element;
    private final OffscreenCanvas m_element;

    //handrey
    //private final Context2D m_context;
    private final OffscreenCanvasRenderingContext2D m_context;

    public ScratchPad(final int wide, final int high) {
        m_wide = wide;

        m_high = high;

        if (LienzoCore.IS_CANVAS_SUPPORTED) {
            //handrey
//            m_element = (HTMLCanvasElement) DomGlobal.document.createElement("canvas");
//            m_element.width = wide;
//            m_element.height = high;

            m_element = new OffscreenCanvas(wide, high);

            //handrey
            //m_context = new Context2D(m_element);
            m_context = Js.cast(m_element.getContext("2d"));
        } else {
            m_element = null;

            m_context = null;
        }
    }

    public final void clear() {
        //handrey
        //Context2D context = getContext();
        OffscreenCanvasRenderingContext2D context = getContext();

        if (null != context) {
            context.clearRect(0, 0, m_wide, m_high);
        }
    }

    public final void setPixelSize(final int wide, final int high) {
        m_element.width = m_wide = wide;

        m_element.height = m_high = high;
    }

    //handrey
//    public final HTMLCanvasElement getElement() {
//        return m_element;
//    }
    public final OffscreenCanvas getElement() {
        return m_element;
    }

    public final int getWidth() {
        return m_wide;
    }

    public final int getHeight() {
        return m_high;
    }

    //handrey
//    public final Context2D getContext() {
//        return m_context;
//    }
    public final OffscreenCanvasRenderingContext2D getContext() {
        return m_context;
    }

    //handrey not supported
    public final String toDataURL() {
        if (null != m_element) {
            return toDataURL(m_element);
        } else {
            return "data:,";
        }
    }

    public final String toDataURL(DataURLType mimetype, final double quality) {
        if (null != m_element) {
            if (null == mimetype) {
                mimetype = DataURLType.PNG;
            }
            return toDataURL(m_element, mimetype.getValue(), quality);
        } else {
            return "data:,";
        }
    }

    public static final String toDataURL(final HTMLImageElement element, final double quality) {
        return toDataURL(element, DataURLType.PNG, quality);
    }

    public static final String toDataURL(final HTMLImageElement element, DataURLType mimetype, final double quality) {
        if (null == mimetype) {
            mimetype = DataURLType.PNG;
        }
        ScratchPad canvas = new ScratchPad(element.width, element.height);

        canvas.getContext().drawImage(element, 0, 0, element.width, element.height);

        return canvas.toDataURL(mimetype, quality);
    }

    public static final String toDataURL(final HTMLImageElement element) {
        final ScratchPad canvas = new ScratchPad(element.width, element.height);

        canvas.getContext().drawImage(element, 0, 0);

        return canvas.toDataURL();
    }

    //handrey
//    private static final String toDataURL(final HTMLCanvasElement element) {
//        return element.toDataURL(null); // @FIXME Make sure this accepts null (mdp)
//    }
    private static final String toDataURL(final OffscreenCanvas element) {
        String[] dataURL = new String[]{""};
        element.convertToBlob(null).then(blob -> {
//            FileReader fileReader = new FileReader();
//            fileReader.readAsDataURL(blob);
//            dataURL[0] = fileReader.result.asString();
            dataURL[0] = URL.createObjectURL(blob);
            return null;
        });


        return dataURL.toString();
    }

    //handrey
//    private static final String toDataURL(HTMLCanvasElement element, String mimetype, double quality) {
//        return element.toDataURL(mimetype, quality);
//    }
    private static final String toDataURL(OffscreenCanvas element, String mimetype, double quality) {
        final OffscreenCanvas.ConvertToBlobOptionsType convertToBlobOptionsType = OffscreenCanvas.ConvertToBlobOptionsType.create();
        convertToBlobOptionsType.setType(mimetype);
        convertToBlobOptionsType.setQuality(quality);

        String[] dataURL = new String[]{""};
        element.convertToBlob(convertToBlobOptionsType).then(blob -> {
//            FileReader fileReader = new FileReader();
//            fileReader.readAsDataURL(blob);
//            dataURL[0] = fileReader.result.asString();
            dataURL[0] = URL.createObjectURL(blob);
            return null;
        });

        return dataURL.toString();
    }

    //handrey
    public final static boolean path(final OffscreenCanvasRenderingContext2D context, PathPartListJSO list, boolean beginPath) {
        if (list == null) {
            return false;
        }
        int leng = list.length();
        if (leng < 1) {
            return false;
        }
        int indx = 0;
        boolean fill = false;
        if (beginPath) {
            context.beginPath();
        }
        while (indx < leng) {
            PathPartEntryJSO e = list.get(indx++);
            double[] p = e.getPoints();

            switch (e.getCommand()) {
                case 1:
                    context.lineTo(p[0], p[1]);
                    break;
                case 2:
                    context.moveTo(p[0], p[1]);
                    break;
                case 3:
                    context.bezierCurveTo(p[0], p[1], p[2], p[3], p[4], p[5]);
                    break;
                case 4:
                    context.quadraticCurveTo(p[0], p[1], p[2], p[3]);
                    break;
                case 5:
                    context.ellipse(p[0], p[1], p[2], p[3], p[6], p[4], p[4] + p[5],
                                    (1 - p[7]) > 0);
                    break;
                case 6:
                    if (beginPath) {
                        context.closePath();
                    }
                    fill = true;
                    break;
                case 7:
                    context.arcTo(p[0], p[1], p[2], p[3], p[4]);
                    break;
            }
        }
        return fill;
    }

    public static ImageDataPixelColor getImageDataPixelColor(final OffscreenCanvasRenderingContext2D context,final int x, final int y) {
        return new ImageDataPixelColor(context.getImageData(x, y, 1, 1));
    }
}
