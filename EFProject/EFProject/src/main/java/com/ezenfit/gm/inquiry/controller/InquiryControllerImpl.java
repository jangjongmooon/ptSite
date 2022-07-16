package com.ezenfit.gm.inquiry.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezenfit.gm.inquiry.service.InquiryService;
import com.ezenfit.gm.vo.InquiryCommentVO;
import com.ezenfit.gm.vo.InquiryVO;
import com.ezenfit.gm.vo.Criteria;
import com.ezenfit.gm.vo.PageMaker;



//-----------------------------------------------------------------------------------------------------------
// public class InquiryControllerImpl implements InquiryController
//-----------------------------------------------------------------------------------------------------------
@Controller("inquiryController")
public class InquiryControllerImpl implements InquiryController {
	
	private static final Logger logger = LoggerFactory.getLogger(InquiryControllerImpl.class);
	
	@Autowired
	private InquiryService inquiryService;
	
	@Autowired
	private InquiryVO inquiryVO;
	
	private static String ARTI_IMAGE_REPO = "C:\\data\\inquiry_image";
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 목록 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/inquiry/inquiry.do", method=RequestMethod.GET)
	public ModelAndView inquiryList(Criteria cri, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		List<InquiryVO> inquiryList = inquiryService.inquiryList(cri);	// 문의하기 목록 보여주기	
		System.out.println("inquiryList ==> " + inquiryList);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(inquiryService.listCount()); 
		
		ModelAndView mav = new ModelAndView("/inquiry/inquiry");
		mav.addObject("inquiryList", inquiryList);
		mav.addObject("pageMaker", pageMaker);
		return mav;
		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 글 작성 페이지 이동
	//-----------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/inquiry/inqWrite.do", method=RequestMethod.GET)
	public ModelAndView inqWriteForm(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/inquiry/inqWrite");
		return mav;
		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 작성하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/addWrite.do", method=RequestMethod.POST)
	   public ModelAndView addInquiry(@ModelAttribute("articleVO") InquiryVO inquiryVO, HttpServletRequest request, HttpServletResponse response)
	         throws Exception {

	      request.setCharacterEncoding("utf-8");
	      response.setContentType("text/html;charset=utf-8");
	        
	      System.out.println("Controller에서 받은 ArticleVO ==> " + inquiryVO);
	      int result = inquiryService.addInquiry(inquiryVO);
	      ModelAndView mav = new ModelAndView("redirect:/inquiry/inquiry.do");
	      return mav;


	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 게시글현황 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/inqStatus.do", method=RequestMethod.GET)
	public ModelAndView inqSituation(@RequestParam("ef_i_no") int ef_i_no, HttpServletRequest request, HttpServletResponse response)
	         throws Exception {
		
		System.out.println("Controller에서 받은 ef_i_no  ==> " + ef_i_no);
		
		inquiryVO = inquiryService.inqSituation(ef_i_no);
		
		List<InquiryCommentVO> inquiryCommentList = inquiryService.commentList(ef_i_no);
		System.out.println("Controller에서 받은 inquiryCommentVO  ==> " + ef_i_no);
		System.out.println("문의하기 불러오기 ==> " + inquiryVO);
		System.out.println("Controller에서 받은 inquiryCommentList  ==> " + inquiryCommentList);
		//mav = new ModelAndView("redirect:/member/inqUpdate.do");	--잘못된 경로
		ModelAndView mav = new ModelAndView("/inquiry/inqStatus");
		mav.addObject("inquiry", inquiryVO);
		mav.addObject("inquiryCommentList", inquiryCommentList);
		
		return mav;		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 수정반영하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/updateInquiry.do", method=RequestMethod.POST)
	public ModelAndView updateReflect(@ModelAttribute("inquiryVO") InquiryVO inquiryVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModelAndView mav = new ModelAndView();
		System.out.println("수정반영하기");
		Map<String, String> inquiryMap	= upload(request, response);
		System.out.println("bookInfoMap ==> " + inquiryMap);
		inquiryVO.setEf_i_no(Integer.parseInt(inquiryMap.get("ef_i_no")));
		inquiryVO.setEf_id(inquiryMap.get("ef_id"));
		inquiryVO.setEf_content(inquiryMap.get("ef_content"));
		inquiryVO.setEf_imageFileName(inquiryMap.get("ef_imageFileName"));
		inquiryVO.setEf_i_title(inquiryMap.get("ef_i_title"));
		
		System.out.println("Controller에서 받은 inquiryVO  ==> " + inquiryVO);
		
		int result = inquiryService.updateReflect(inquiryVO);
		System.out.println("updateReflect Controller에서 받은 result 결과 ==> " + result);
		mav = new ModelAndView("redirect:/inquiry/inqStatus.do");
		
		int ef_i_no = inquiryVO.getEf_i_no();
	    mav.addObject("ef_i_no", ef_i_no);
		
		return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/removeArticle.do", method=RequestMethod.POST)
	public ModelAndView deleteInquiry(@RequestParam("ef_i_no") int ef_i_no, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("Controller에서 받은 ef_i_no  ==> " + ef_i_no);
		
		ModelAndView mav = new ModelAndView();
		int result = inquiryService.deleteInquiry(ef_i_no);
		mav = new ModelAndView("redirect:/inquiry/inquiry.do");	
		
		return mav;		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 추가하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/addComment.do", method=RequestMethod.POST)
	public ModelAndView addComment(@ModelAttribute("inquiryCommentVO") InquiryCommentVO inquiryCommentVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		  request.setCharacterEncoding("utf-8");
	      response.setContentType("text/html;charset=utf-8");
	        
	      ModelAndView mav = new ModelAndView();
	      System.out.println("Controller에서 받은 inquiryCommentVO ==> " + inquiryCommentVO);
			
	      int result = inquiryService.addComment(inquiryCommentVO);
	      System.out.println("addReply Controller에서 받은 result 결과 ==> " + result);
			
	      mav = new ModelAndView("redirect:/inquiry/inqStatus.do");	      	      
	      
	      int ef_i_no = inquiryCommentVO.getEf_i_no();
	      mav.addObject("ef_i_no", ef_i_no);
	      
	      return mav;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 추가
	//-----------------------------------------------------------------------------------------------------------	
	@Override
	@RequestMapping(value="/inquiry/addInquiry.do", method=RequestMethod.POST)
	public ModelAndView addContact(@ModelAttribute("inquiryVO") InquiryVO inquiryVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		System.out.println("addContact ==>" + inquiryVO);

		ModelAndView mav = new ModelAndView();
		
		Map<String, String> inquiryMap	= upload(request, response);
		System.out.println("inquiryInfoMap ==> " + inquiryMap);
		
		inquiryVO.setEf_id(inquiryMap.get("ef_id"));
		inquiryVO.setEf_content(inquiryMap.get("ef_content"));
		inquiryVO.setEf_imageFileName(inquiryMap.get("ef_imageFileName"));
		inquiryVO.setEf_i_title(inquiryMap.get("ef_i_title"));
		
		System.out.println("inquiryVO ==> " + inquiryVO);
		int result = inquiryService.addContact(inquiryVO); 	
		mav = new ModelAndView("redirect:/inquiry/inquiry.do");
		mav.addObject("inquiry", inquiryVO);
			
		return mav;
	}	
	
	//-----------------------------------------------------------------------------------------------------------
	// 파일 올리기 메서드
	//-----------------------------------------------------------------------------------------------------------
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
			
			Map<String, String> inquiryMap = new HashMap<String, String>();
			String encoding	=	"utf-8";
			
			// 업로드할 파일의 경로를 지정한다.
			File				currentDirPath		= new File(ARTI_IMAGE_REPO);
			
			DiskFileItemFactory	factory				= new DiskFileItemFactory();
			
			// 파일 경로를 설정한다.
			factory.setRepository(currentDirPath);
			
			// 업로드될 파일의 최대 크기를 설정한다.
			factory.setSizeThreshold(1024*1024*1024);
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			try {
				// request객체에서 매개 변수를 List로 가져온다.
				List items = upload.parseRequest(request);
				
				for(int i = 0; i < items.size(); i++) {
					//	파일 업로드 창에서 업로드된 항목들을 하나씩 가져와서 작업을 한다.
					FileItem fileItem = (FileItem) items.get(i);
					
					// 폼 필드이면 전송된 매개 변수의 값을 출력한다.
					if(fileItem.isFormField()) {
						inquiryMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
					} else { // 폼 필드가 아니면 파일 업로드 기능을 실행한다.
						// 업로드한 파일의 이름을 가져온다.
						// 파일의 사이즈가 0보다 큰 경우만 업로드를 한다.
						if(fileItem.getSize() > 0) {
							// 변수.lastIndexOf(검색값) => 변수에서 검색값 들 중에서 마지막 것을 말한다.
							int idx = fileItem.getName().lastIndexOf("\\");
							if(idx == -1) { // 이런(\\) 경로가 아니라면 / 경로의 마지막에서 파일이름을 찾는다.
								idx = fileItem.getName().lastIndexOf("/");
							}
							
							// 경로에서 파일 이름을 추출한다.
							// "ABCDEFGHIJ"
							// substring(4) = > 인덱스번호 4이상 모든 값 => EFGHIJ
							// substring(3, 7) => 인덱스번호 3번 부터 7번 전까지 => DEFG
							String fileName = fileItem.getName().substring(idx+1);
							
							
							while(true) {
								String fileName1 = fileName + Math.random();
								String ef_imageFileName = fileName1;
								int result = inquiryService.imgContact(ef_imageFileName);
								if(result == 1) {
									fileName1 = "";
									continue;
								} else {
									fileName = fileName1;
									break;
								}
							}
							
							// 업로드한 파일의 이름으로 저장소(currentDirPath)에 파일을 업로드 한다.
							// File uploadFile = new File(currentDirPath + "\\" + fileName);
							// 파일이름이 중복되면 마지막에 올린 파일만 존재하게 되므로 임시 파일에 저장시키고,
							// 책 번호를 부여받게 되면 책 번호의 폴더를 만들어서 저장시키도록 한다.
							// upload()를 호출한 곳으로 bookInfoMap에 파일 정보를 넣어준다.
							inquiryMap.put(fileItem.getFieldName(), fileName);
							
							File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
							fileItem.write(uploadFile);
						}
						
					} // End - if
					
				} // End - for
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return inquiryMap;
		}

	//-----------------------------------------------------------------------------------------------------------
	// 답글 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	@RequestMapping(value="/inquiry/deleteComment.do", method=RequestMethod.GET)
	public ModelAndView deleteComment(@RequestParam("ef_i_no") int ef_i_no, @RequestParam("ef_comment") int ef_comment, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		System.out.println("Controller에서 받은 ef_comment  ==> " + ef_comment);
		
		ModelAndView mav = new ModelAndView();
		int result = inquiryService.deleteComment(ef_comment);
		
		mav = new ModelAndView("redirect:/inquiry/inqStatus.do");	
		
	    mav.addObject("ef_i_no", ef_i_no);
	      
		return mav;	
	}	
	
	
	
} // End - public class InquiryControllerImpl implements InquiryController
