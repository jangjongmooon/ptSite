package com.ezenfit.gm.freeboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezenfit.gm.freeboard.service.FreeBoardService;
import com.ezenfit.gm.vo.Criteria;
import com.ezenfit.gm.vo.FreeBoardCommentVO;
import com.ezenfit.gm.vo.FreeBoardVO;
import com.ezenfit.gm.vo.PageMaker;

//-----------------------------------------------------------------------------------------------------------
//public class FreeBoardControllerImpl implements FreeBoardController
//-----------------------------------------------------------------------------------------------------------
@Controller("freeBoardController")
public class FreeBoardControllerImpl implements FreeBoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(FreeBoardControllerImpl.class);
	
	@Autowired
	private FreeBoardService freeBoardService;
	
	@Autowired
	private FreeBoardVO freeBoardVO;
	
	private static String FREEBOARD_IMAGE_REPO = "C:\\data\\freeBoard_image";
	
	//-----------------------------------------------------------------------------------------------------------
	// ???????????????(freeboard) ??????
	//-----------------------------------------------------------------------------------------------------------		
	@RequestMapping(value="/freeboard/freeboardMenu.do", method=RequestMethod.GET)
	public ModelAndView freeBoardList(Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		List<FreeBoardVO> freeBoardList = freeBoardService.freeBoardList(cri);
		System.out.println("freeBoardList ==> " + freeBoardList);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(freeBoardService.listCount()); 
		
		ModelAndView mav = new ModelAndView("/freeboard/freeboard");
		mav.addObject("freeBoardList", freeBoardList);
		mav.addObject("pageMaker", pageMaker);
		return mav;
	}

	//-----------------------------------------------------------------------------------------------------------
	// ??????????????? ??? ?????? ????????? ??????
	//-----------------------------------------------------------------------------------------------------------		
	@RequestMapping(value="/freeboard/freeboardWritePosts.do", method=RequestMethod.GET)
	public ModelAndView freeboardWritePosts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/freeboard/freeboardWritePosts");
		return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// ????????? ??????
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/freeboard/addPosts.do", method=RequestMethod.POST)
	public ModelAndView addPosts(@ModelAttribute("FreeBoardVO") FreeBoardVO freeBoardVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		System.out.println("addPosts ==>" + freeBoardVO);

		ModelAndView mav = new ModelAndView();
		
		Map<String, String> freeBoardMap	= upload(request, response);//
		
		System.out.println("bookInfoMap ==> " + freeBoardMap);
		freeBoardVO.setEf_id(freeBoardMap.get("ef_id"));
		freeBoardVO.setFreeBoard_publicScope(freeBoardMap.get("freeBoard_publicScope"));
		freeBoardVO.setFreeBoard_content(freeBoardMap.get("freeBoard_content"));
		freeBoardVO.setFreeBoard_imageFileName(freeBoardMap.get("freeBoard_imageFileName"));
		freeBoardVO.setFreeBoard_title(freeBoardMap.get("freeBoard_title"));
		freeBoardVO.setFreeBoard_notice(freeBoardMap.get("freeBoard_notice"));
		System.out.println("freeBoardVO ==> " + freeBoardVO);
		int result = freeBoardService.addPosts(freeBoardVO);
		System.out.println("????????? ?????? controller ?????? freeboard_no ==> " + result);

		
		if(freeBoardMap.get("freeBoard_imageFileName") != null && freeBoardMap.get("freeBoard_imageFileName").length() != 0) {
			File srcFile = new File(FREEBOARD_IMAGE_REPO + "\\" + "temp" + "\\" + freeBoardMap.get("freeBoard_imageFileName"));
			File destDir = new File(FREEBOARD_IMAGE_REPO + "\\" + result);
			
			destDir.mkdirs();
			FileUtils.moveFileToDirectory(srcFile, destDir, true);
		}

		mav = new ModelAndView("redirect:/freeboard/freeboardMenu.do");
		
		
		return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// ????????? ??????
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/freeboard/freeBoardViewPosts.do", method=RequestMethod.GET)
	public ModelAndView freeBoardViewPosts(@RequestParam("freeBoard_no") int freeBoard_no, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// System.out.println("freeBoardView freeBoard_no ==> " + freeBoard_no);
		freeBoardVO = freeBoardService.freeBoardViewPosts(freeBoard_no);
		// System.out.println("freeBoardView Controller ==> " + freeBoardVO);
		List<FreeBoardCommentVO> freeBoardViewCommentsList = freeBoardService.freeBoardViewCommentsList(freeBoard_no);
		ModelAndView mav = new ModelAndView("/freeboard/freeboardViewPosts");
		mav.addObject("freeboard", freeBoardVO);
		mav.addObject("freeBoardViewCommentsList", freeBoardViewCommentsList);
		return mav;
	} 	
	
	
	//-----------------------------------------------------------------------------------------------------------
	// ????????? ?????? ??????
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	@RequestMapping(value="/freeboard/modPosts.do", method=RequestMethod.POST)
	public ModelAndView modPosts(@ModelAttribute("FreeBoardVO") FreeBoardVO freeBoardVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// System.out.println("???????????? ==> " + freeBoardVO);
		ModelAndView mav = new ModelAndView();
		Map<String, String> freeBoardMap	= upload(request, response);
		// System.out.println("???????????? freeBoardMap ==> " + freeBoardMap.get("freeBoard_imageFileName"));
		freeBoardVO.setFreeBoard_no(Integer.parseInt(freeBoardMap.get("freeBoard_no")));
		freeBoardVO.setEf_id(freeBoardMap.get("ef_id"));
		freeBoardVO.setFreeBoard_publicScope(freeBoardMap.get("freeBoard_publicScope"));
		freeBoardVO.setFreeBoard_content(freeBoardMap.get("freeBoard_content"));
		if(freeBoardMap.get("freeBoard_imageFileName") == null || freeBoardMap.get("freeBoard_imageFileName").length() == 0) { // ??????????????? ????????? ????????? ????????? ????????? ???????????? ??????
			freeBoardVO.setFreeBoard_imageFileName(freeBoardMap.get("originalImageFileName"));
		} else {
			freeBoardVO.setFreeBoard_imageFileName(freeBoardMap.get("freeBoard_imageFileName"));
		}
		freeBoardVO.setFreeBoard_title(freeBoardMap.get("freeBoard_title"));
		freeBoardVO.setFreeBoard_notice(freeBoardMap.get("freeBoard_notice"));
		// System.out.println("???????????? freeBoardVO ==> " + freeBoardVO.getFreeBoard_imageFileName());
		
		int result = freeBoardService.modPosts(freeBoardVO);
		
		if(freeBoardMap.get("freeBoard_imageFileName") != null && freeBoardMap.get("freeBoard_imageFileName").length() != 0) {
			File srcFile = new File(FREEBOARD_IMAGE_REPO + "\\" + "temp" + "\\" + freeBoardMap.get("freeBoard_imageFileName"));
			File destDir = new File(FREEBOARD_IMAGE_REPO + "\\" + freeBoardVO.getFreeBoard_no());
			
			
			File	oldFile	= new File(FREEBOARD_IMAGE_REPO + "\\" + freeBoardVO.getFreeBoard_no() + "\\" + freeBoardMap.get("originalImageFileName"));
			oldFile.delete();
			
			destDir.mkdirs();
			FileUtils.moveFileToDirectory(srcFile, destDir, true);

		}
		
		mav = new ModelAndView("redirect:/freeboard/freeBoardViewPosts.do");
		mav.addObject("freeBoard_no", freeBoardVO.getFreeBoard_no());
		return mav;
	}

	//-----------------------------------------------------------------------------------------------------------
	// ????????? ??????
	//-----------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/freeboard/freeBoardDeletePosts.do", method=RequestMethod.GET)
	public ModelAndView freeBoardDeletePosts(@RequestParam("freeBoard_no") int freeBoard_no, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		System.out.println("freeBoardViewPosts freeBoard_no ==> " + freeBoard_no);
		
		int result = freeBoardService.freeBoardDeletePosts(freeBoard_no);
		System.out.println("freeBoardViewPosts Controller ==> " + result);
		
		File imgDir = new File(FREEBOARD_IMAGE_REPO + "\\" + freeBoard_no);
		if(imgDir.exists()) {
			FileUtils.deleteDirectory(imgDir);
		}
		
		ModelAndView mav = new ModelAndView("redirect:/freeboard/freeboardMenu.do");
		return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// ?????? ????????????
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/freeboard/addComments.do", method=RequestMethod.POST)
	public ModelAndView addComments(@ModelAttribute("FreeBoardCommentVO") FreeBoardCommentVO freeBoardCommentVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out.println("addPosts freeBoardCommentVO ==> " + freeBoardCommentVO);
			
		int result = freeBoardService.addComments(freeBoardCommentVO);
		System.out.println("addPosts Controller ==> " + result);		
			
		int freeBoard_no = freeBoardCommentVO.getFreeBoard_no();
			
		ModelAndView mav = new ModelAndView("redirect:/freeboard/freeBoardViewPosts.do");
		mav.addObject("freeBoard_no", freeBoard_no);
			
		return mav;
	}
		
	//-----------------------------------------------------------------------------------------------------------
	// ?????? ??????
	//-----------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/freeboard/deleteComments.do", method=RequestMethod.GET)
	public ModelAndView deleteComments(@RequestParam("freeBoard_commentNO") int freeBoard_commentNO, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		// ????????? ????????? ??? ????????? ????????? ????????? ??????????????? ??????
		FreeBoardCommentVO freeBoardCommentVO = freeBoardService.fbcvo(freeBoard_commentNO);
		System.out.println("????????? ????????? ???????????? ?????? ?????? ?????? ==> " + freeBoardCommentVO);
		int freeBoard_no = freeBoardCommentVO.getFreeBoard_no();
		
		int result = freeBoardService.deleteComments(freeBoardCommentVO);
		System.out.println("DeleteComments Controller ?????? ==> " + result);
		
		ModelAndView mav = new ModelAndView("redirect:/freeboard/freeBoardViewPosts.do");
		mav.addObject("freeBoard_no", freeBoard_no);
		
		return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// ?????? ????????????
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/freeboard/addReply.do", method=RequestMethod.POST)
	public ModelAndView addReply(FreeBoardCommentVO freeBoardCommentVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("???????????? vo ==> " + freeBoardCommentVO);
		// ??????????????? ??????????????? ??????
		int result = freeBoardService.addComments(freeBoardCommentVO);
		ModelAndView mav = new ModelAndView("redirect:/freeboard/freeBoardViewPosts.do");
		mav.addObject("freeBoard_no", freeBoardCommentVO.getFreeBoard_no());
			
		return mav;
	}

	
	//-----------------------------------------------------------------------------------------------------------
	// ?????? ????????? ????????????
	//-----------------------------------------------------------------------------------------------------------
	@ResponseBody
	@RequestMapping(value="/freeboard/replyList.do", method=RequestMethod.POST)
	public List<FreeBoardCommentVO> replyList(@RequestParam("freeBoard_commentParentsNO") int freeBoard_commentParentsNO, FreeBoardCommentVO freeBoardCommentVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("??????????????? ???????????? ==> " + freeBoard_commentParentsNO);
		
		List<FreeBoardCommentVO> replyList = freeBoardService.replyList(freeBoard_commentParentsNO);
		System.out.println("?????? ????????? ?????? ==> " + replyList);
		
		return replyList;
	}
	

	//-----------------------------------------------------------------------------------------------------------
	// ?????? ????????? ?????????
	//-----------------------------------------------------------------------------------------------------------
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		Map<String, String> freeBoardMap = new HashMap<String, String>();
		String encoding	=	"utf-8";
		
		// ???????????? ????????? ????????? ????????????.
		File				currentDirPath		= new File(FREEBOARD_IMAGE_REPO);
		
		DiskFileItemFactory	factory				= new DiskFileItemFactory();
		
		// ?????? ????????? ????????????.
		factory.setRepository(currentDirPath);
		
		// ???????????? ????????? ?????? ????????? ????????????.
		factory.setSizeThreshold(1024*1024*1024);
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			// request???????????? ?????? ????????? List??? ????????????.
			List items = upload.parseRequest(request);
			
			for(int i = 0; i < items.size(); i++) {
				//	?????? ????????? ????????? ???????????? ???????????? ????????? ???????????? ????????? ??????.
				FileItem fileItem = (FileItem) items.get(i);
				
				// ??? ???????????? ????????? ?????? ????????? ?????? ????????????.
				if(fileItem.isFormField()) {
					freeBoardMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else { // ??? ????????? ????????? ?????? ????????? ????????? ????????????.
					// ???????????? ????????? ????????? ????????????.
					// ????????? ???????????? 0?????? ??? ????????? ???????????? ??????.
					if(fileItem.getSize() > 0) {
						// ??????.lastIndexOf(?????????) => ???????????? ????????? ??? ????????? ????????? ?????? ?????????.
						int idx = fileItem.getName().lastIndexOf("\\");
						if(idx == -1) { // ??????(\\) ????????? ???????????? / ????????? ??????????????? ??????????????? ?????????.
							idx = fileItem.getName().lastIndexOf("/");
						}
						
						// ???????????? ?????? ????????? ????????????.
						// "ABCDEFGHIJ"
						// substring(4) = > ??????????????? 4?????? ?????? ??? => EFGHIJ
						// substring(3, 7) => ??????????????? 3??? ?????? 7??? ????????? => DEFG
						String fileName = fileItem.getName().substring(idx+1);
						
						
						// ???????????? ????????? ???????????? ?????????(currentDirPath)??? ????????? ????????? ??????.
						// File uploadFile = new File(currentDirPath + "\\" + fileName);
						// ??????????????? ???????????? ???????????? ?????? ????????? ???????????? ????????? ?????? ????????? ???????????????,
						// ??? ????????? ???????????? ?????? ??? ????????? ????????? ???????????? ?????????????????? ??????.
						// upload()??? ????????? ????????? bookInfoMap??? ?????? ????????? ????????????.
						freeBoardMap.put(fileItem.getFieldName(), fileName);
						
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);
					}
					
				} // End - if
				
			} // End - for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return freeBoardMap;
	}

	
	
	
} // End - public class FreeBoardControllerImpl implements FreeBoardController
