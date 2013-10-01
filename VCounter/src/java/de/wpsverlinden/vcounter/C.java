/**
 *
 * VCounter - Der Besucherzähler ohne JavaScript!
 * Copyright (C) 2013 Oliver Verlinden (http://wps-verlinden.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.wpsverlinden.vcounter;

import de.wpsverlinden.vcounter.dao.StatDAO;
import de.wpsverlinden.vcounter.dao.HitDAO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "c", urlPatterns = {"/c"})
public class C extends HttpServlet {

    @EJB
    StatDAO sdao;

    @EJB
    HitDAO hdao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(true);
            long cId = Long.parseLong(request.getParameter("id"));
            int type = Integer.parseInt(request.getParameter("t"));
            int size = Integer.parseInt(request.getParameter("s"));
            
            try {
                sdao.registerStatFor(cId, session.isNew());
                hdao.registerHitFor(cId, session, request);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            ServletContext cntx = getServletContext();
            // Get the absolute path of the image
            String filepath = cntx.getRealPath("resources/images/counter/" + type + "_" + size + ".png");
            File img = new File(filepath);
            String mime = cntx.getMimeType(filepath);
            if (!img.exists() || mime == null) {
                sendErrorImage(request, response);
                return;
            }

            response.setContentType(mime);
            response.setContentLength((int) img.length());

            FileInputStream in = new FileInputStream(img);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }

        } catch (NumberFormatException e) {
            sendErrorImage(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet ho handle the visitor tracking";
    }

    private void sendErrorImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletContext cntx = getServletContext();
            String filepath = cntx.getRealPath("resources/images/counter/error.png");
            File img = new File(filepath);
            String mime = cntx.getMimeType(filepath);

            response.setContentType(mime);
            response.setContentLength((int) img.length());

            FileInputStream in = new FileInputStream(img);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        } catch (Exception e) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            response.setContentType("image/png");
            try (OutputStream outputStream = response.getOutputStream()) {
                ImageIO.write(image, "png", outputStream);
            } catch (IOException ex) {
                ;
            }
        }
    }
}
