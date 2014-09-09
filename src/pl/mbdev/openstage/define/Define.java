package pl.mbdev.openstage.define;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Define
 */
@WebServlet(description = "Uses Google services to fetch definition of agiven word.", urlPatterns = { "/Define" })
public class Define extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Define() {
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String previousWord = request.getParameter("word");
		if (previousWord == null || previousWord.equals(""))
			previousWord = "orthogonal";
		
		new GoogleDefine(out, request.getRequestURL().toString(), previousWord);
		
		out.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintWriter out = new PrintWriter(System.out);
		new GoogleDefine(out, "localhost", "orthogonal");
		out.close();
	}
}
