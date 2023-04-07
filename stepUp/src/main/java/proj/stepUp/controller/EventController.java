package proj.stepUp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import proj.stepUp.service.EventService;
import proj.stepUp.util.PagingUtil;
import proj.stepUp.vo.EventBoardVO;
import proj.stepUp.vo.SearchVO;
import proj.stepUp.vo.UserVO;


@RequestMapping(value="/event")	
@Controller

public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/event.do", method = RequestMethod.GET)
	public String event(Model model, SearchVO svo) {
		
		
		int nowPage = 1;
		if(svo.getNowPage() != 0 ) {
			nowPage = svo.getNowPage();
		}
		
		List<EventBoardVO> cntTotal = eventService.cntTotal(svo);
		int totalCnt = 0;
		if(cntTotal.size()>0) {
			totalCnt = cntTotal.get(0).getTotal();
		}
		
		PagingUtil paging = new PagingUtil(totalCnt,nowPage, 10);
		
		
		List<EventBoardVO> list = eventService.list(svo);
		
		System.out.println("getStart:::"+paging.getStart());
		System.out.println("totalCnt:::"+totalCnt);
		System.out.println("getPerPage:::"+paging.getPerPage());
		System.out.println("getNowPage:::"+paging.getNowPage());
		System.out.println("getEndPage:::"+paging.getEndPage());
		
		model.addAttribute("blist", list);
		model.addAttribute("paging", paging);
		return "event/event_list";
	}
	
	@RequestMapping(value="/event_write.do", method = RequestMethod.GET)
	public String eventwrite() {
		
		return "event/event_write";
	}
	
	@RequestMapping(value="/event_write.do", method = RequestMethod.POST)
	public String eventwrite(EventBoardVO vo, HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		UserVO loginUserVO = (UserVO)session.getAttribute("login");
		vo.setUserIndex(loginUserVO.getUserIndex());
		int result = eventService.insert(vo);
		
		return "redirect:/event/event_view.do?eventIndex="+vo.getEventIndex();
	}
	
	@RequestMapping(value="/event_view.do")
	public String eventview(int eventIndex, Model model) {
		EventBoardVO vo = eventService.selectByIndex(eventIndex);
		model.addAttribute("vo", vo);
		
		return "event/event_view";
	}
	
	@RequestMapping(value="/event_modify.do", method = RequestMethod.GET)
	public String modify(int eventIndex, Model model) {
		EventBoardVO vo = eventService.selectByIndex(eventIndex);
		model.addAttribute("vo", vo);
		
		return "event/event_modify";
	}
	
	@RequestMapping(value="/event_modify.do", method = RequestMethod.POST)
	public String modify(EventBoardVO vo) {
		
		int result = eventService.update(vo);
		if(result>0) {
			return "redirect:/event/event_view.do?eventIndex="+vo.getEventIndex();
		}else {
			return "redirect:/event/event_view.do?eventIndex="+vo.getEventIndex()+"&updateYN=N";
		}
	}
	
	@RequestMapping(value="/event_delete.do", method = RequestMethod.POST)
	public String delete(int eventIndex) {
		int result = eventService.delete(eventIndex);
		
		return "redirect:/event/event.do";
	}

}
