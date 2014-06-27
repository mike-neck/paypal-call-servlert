package ikemen;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright 2014Shinya Mochida
 * <p/>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AsyncServlet extends HttpServlet {

    private static final String P = "<p>";

    private static final String PE = "</p>";

    private static final String NEW_LINE = "<br/>";

    private static final String SPAN_HEAD = "<span class='head'>";

    private static final String SPAN_DATA = "<span class='data'>";

    private static final String SPAN_END  = "</span>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doAsync(req);
        execute(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doAsync(req);
        execute(req, resp);
    }

    private void execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append('\n')
                .append("<title>AsyncServlet</title>")
                .append('\n')
                .append("<style type='text/css'>")
                .append("p span.head{background-color : #ccf; width : 300px; margin-right : 10px; padding : 10px; border-radius : 10px;}")
                .append('\n')
                .append("p span.data{background-color : #fff; width : 480px; padding : 10px; margin-top : 10px; margin : bottom : 10px;}")
                .append('\n')
                .append("</style>")
                .append("</head>")
                .append('\n')
                .append("<body>")
                .append("<article>")
                .append("<h1>Attributes</h1>")
                .append(P)
                .append(SPAN_HEAD).append("method").append(SPAN_END)
                .append(SPAN_DATA).append(req.getMethod()).append(SPAN_END)
                .append(NEW_LINE)
                .append(PE)
                .append("<h1>Header</h1>")
                .append(P);

        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = req.getHeader(name);
            builder.append(SPAN_HEAD)
                    .append(name)
                    .append(SPAN_END)
                    .append(SPAN_DATA)
                    .append(value)
                    .append(SPAN_END)
                    .append(NEW_LINE);
        }

        builder.append(PE)
                .append("</article>")
                .append("<footer>")
                .append("<script>")
                .append("var now = new Date(),y=now.getFullYear(),M=now.getMonth()+1,d=now.getDate(),")
                .append("h=now.getHours(),m=now.getMinutes(),s=now.getSeconds(),S=now.getMilliseconds(),")
                .append("date=y+'/'+M+'/'+d+' '+h+':'+m+':'+s+'.'+S;")
                .append("document.write(date);")
                .append("</script>")
                .append("</footer>")
                .append("</body>")
                .append("</html>");
        String html = builder.toString();
        res.setContentLength(html.getBytes().length);
        res.getWriter().append(html).flush();
    }

    private void doAsync(HttpServletRequest req) {
        AsyncContext context = req.startAsync();
        context.start(new Worker(context));
    }

    private void consoleWrite (String msg) {
        String now = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS").format(
                Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).getTime());
        System.out.println('[' + now + "] " + msg);
    }

    private class Worker implements Runnable {

        private final AsyncContext context;

        private Worker(AsyncContext context) {
            this.context = context;
        }

        @Override
        public void run() {
            List<Method> methods = Arrays.asList(HttpServletRequest.class.getMethods());
            for (Method method : methods) {
                consoleWrite(method.getName());
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException ignored) {
                }
            }
            consoleWrite("AsyncWork finished!");
            context.complete();
        }
    }
}
