package com.moshi.impl.webapp

import com.moshi.impl.captcha.CaptchaServiceSingleton
import com.octo.captcha.service.CaptchaServiceException
import com.sun.image.codec.jpeg.JPEGCodec
import com.sun.image.codec.jpeg.JPEGImageEncoder

import javax.servlet.ServletException
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.awt.image.BufferedImage

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImageCaptchaServlet extends HttpServlet {
    protected final static Logger logger = LoggerFactory.getLogger(ImageCaptchaServlet.class)

    ImageCaptchaServlet () { super() }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        byte[] captchaChallengeAsJpeg
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream()
        try {
            // get the session id that will identify the generated captcha.
            //the same id must be used to validate the response, the session id is a good candidate!
            String captchaId = httpServletRequest.getSession().getId()
            
            // call the ImageCaptchaService getChallenge method
            BufferedImage challenge =
                    CaptchaServiceSingleton.getInstance().getImageChallengeForID(captchaId,
                            httpServletRequest.getLocale())

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream)
            jpegEncoder.encode(challenge)
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        } catch (CaptchaServiceException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
            return
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray()

        // flush it in the response
        httpServletResponse.setHeader("Cache-Control", "no-store")
        httpServletResponse.setHeader("Pragma", "no-cache")
        httpServletResponse.setDateHeader("Expires", 0)
        httpServletResponse.setContentType("image/jpeg")
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream()
        responseOutputStream.write(captchaChallengeAsJpeg)
        responseOutputStream.flush()
        responseOutputStream.close()
    }
}
