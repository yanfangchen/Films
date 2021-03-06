/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.films.struts.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.films.domain.Film;
import com.films.domain.Filmcomment;
import com.films.domain.Users;
import com.films.service.inter.IFCommService;
import com.films.service.inter.IFilmService;
import com.films.service.inter.ITimeTableService;
import com.films.struts.form.CommentsForm;

/** 
 * MyEclipse Struts
 * Creation date: 11-20-2012
 * 
 * XDoclet definition:
 * @struts.action parameter="flag"
 */
public class MovieAction extends DispatchAction {

	private IFilmService filmService;
	private ITimeTableService timeTableService;
	private IFCommService fcService;
	
	public void setFcService(IFCommService fcService) {
		this.fcService = fcService;
	}

	public void setTimeTableService(ITimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}
	
	public void setFilmService(IFilmService filmService) {
		this.filmService = filmService;
	}
	
	public ActionForward goSingle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String fid = request.getParameter("fid");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(); 
		Film movie = (Film) filmService.findById(Film.class, Integer.valueOf(fid));
		request.setAttribute("movie", movie);		
		request.setAttribute("mtt", timeTableService.findTimetableByFid(fid,sdf.format(date)));
		request.setAttribute("tt", timeTableService.getTimeTable());
		//get comments
		request.setAttribute("comm", fcService.getFilmComments(Integer.valueOf(fid)));
		return mapping.findForward("goSingle");
	}
	
	public ActionForward submitComm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Users user = (Users) request.getSession().getAttribute("loginUser");
		if(user!=null){
			String fid = request.getParameter("fid");
			Film film = (Film) filmService.findById(Film.class, Integer.valueOf(fid));
			CommentsForm cmf = (CommentsForm) form;
			Filmcomment fc = new Filmcomment();
			fc.setUsers(user);
			fc.setFilm(film);
			fc.setCtime(new java.sql.Timestamp(new java.util.Date().getTime()));
			fc.setComments(cmf.getContent());
			fcService.save(fc);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(); 
			Film movie = (Film) filmService.findById(Film.class, Integer.valueOf(fid));
			request.setAttribute("movie", movie);		
			request.setAttribute("mtt", timeTableService.findTimetableByFid(fid,sdf.format(date)));
			request.setAttribute("tt", timeTableService.getTimeTable());
			request.setAttribute("comm", fcService.getFilmComments(Integer.valueOf(fid)));
			return mapping.findForward("goSingle");
		}else{
			return mapping.findForward("firstLogin");
		}
		
	}
}