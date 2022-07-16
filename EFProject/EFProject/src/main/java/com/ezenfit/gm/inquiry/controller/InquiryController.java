package com.ezenfit.gm.inquiry.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezenfit.gm.vo.InquiryCommentVO;
import com.ezenfit.gm.vo.InquiryVO;

//-----------------------------------------------------------------------------------------------------------
// public interface InquiryController
//-----------------------------------------------------------------------------------------------------------
public interface InquiryController {
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 작성하기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView addInquiry(@ModelAttribute("inquiryVO") InquiryVO inquiryVO,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
		
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 게시글현황 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView inqSituation(@RequestParam("ef_i_no") int ef_i_no,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 수정반영하기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView updateReflect(@ModelAttribute("inquiryVO") InquiryVO inquiryVO, 
			HttpServletRequest request, HttpServletResponse response) throws Exception;		
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView deleteInquiry(@RequestParam("ef_i_no") int ef_i_no,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 추가하기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView addComment(@ModelAttribute("inquiryCommentVO") InquiryCommentVO inquiryCommentVO,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 추가
	//-----------------------------------------------------------------------------------------------------------	
	public ModelAndView addContact(@ModelAttribute("inquiryVO") InquiryVO inquiryVO,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	public ModelAndView deleteComment(@RequestParam("ef_i_no") int ef_i_no, @RequestParam("ef_comment") int ef_comment,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
		
		
} // End - public interface InquiryController
