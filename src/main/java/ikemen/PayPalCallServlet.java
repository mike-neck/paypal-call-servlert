package ikemen;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mike
 */
public class PayPalCallServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeResponse(resp);
    }

    private void writeResponse(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.append('{')
                .append('"')
                .append("request")
                .append('"')
                .append(" : ")
                .append('"')
                .append("ok")
                .append('"')
                .append('}')
                .flush();
    }
}
