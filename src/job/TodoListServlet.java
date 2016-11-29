package job;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.*;

import job.PMF;
import job.Job;

@SuppressWarnings("serial")
public class TodoListServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PersistenceManagerFactory factory = PMF.get();
        PersistenceManager manager = factory.getPersistenceManager();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        req.setCharacterEncoding("utf-8");
        String param1 = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        List<Job> list = null;
        if (param1 == null || param1 ==""){
            String query = "select from " + Job.class.getName();
            try {
                list = (List<Job>)manager.newQuery(query).execute();
            } catch(JDOObjectNotFoundException e){}
        } else {
            try {
            	Job data = (Job)manager.getObjectById(Job.class,Long.parseLong(param1));
                list = new ArrayList();
                list.add(data);
            } catch(JDOObjectNotFoundException e){}
        }
        String res = "[";
        if (list != null){
            for(Job data:list){
            	 res += "{id:" + data.getId() + ",title:'" + data.getTitle() + "',desc:'" +
 	                    data.getDesc() + "',target:'" + data.getTarget() +
 	                    "',from_date:'" + data.getFromDate() + 
 	                    "',to_date:'" + data.getToDate() +
 	                    "',isDone:'" + data.getIsDone() +"'},";
            }
        }
        res += "]";
        out.println(res);
        manager.close();
	}
}
