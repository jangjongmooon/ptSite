package com.ezenfit.gm.inquiry.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ezenfit.gm.inquiry.dao.InquiryDAO;
import com.ezenfit.gm.vo.InquiryCommentVO;
import com.ezenfit.gm.vo.InquiryVO;
import com.ezenfit.gm.vo.Criteria;



//-----------------------------------------------------------------------------------------------------------
// public class InquiryServiceImpl implements InquiryService
//-----------------------------------------------------------------------------------------------------------
@Service("inquiryService")
public class InquiryServiceImpl implements InquiryService {
	
	private static final Logger logger = LoggerFactory.getLogger(InquiryServiceImpl.class);
	
	
	@Autowired
	private InquiryDAO inquiryDAO;
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 목록 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	public List<InquiryVO> inquiryList(Criteria cri) throws DataAccessException {
		List<InquiryVO> inquiryList = inquiryDAO.inquiryList(cri);		
		return inquiryList;
	}	
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 리스트 카운트
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	public int listCount() throws DataAccessException {
		
		return inquiryDAO.listCount();
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 작성하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int addInquiry(InquiryVO inquiryVO) throws DataAccessException {
		System.out.println("serviceVO ==>" + inquiryVO);
		return inquiryDAO.addInquiry(inquiryVO);
	}

	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 게시글현황 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public InquiryVO inqSituation(int ef_i_no) throws DataAccessException {
		System.out.println("serviceVO ==>" + ef_i_no);	
		return inquiryDAO.inqSituation(ef_i_no);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 현황 리스트 불러오기
	//-----------------------------------------------------------------------------------------------------------			
	@Override
	public List<InquiryCommentVO> commentList(int ef_i_no) throws DataAccessException {
		System.out.println("serviceVO ==>" + ef_i_no);	
		
		List<InquiryCommentVO> commentList = inquiryDAO.commentList(ef_i_no);		
		return commentList;
	}

	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 수정반영하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int updateReflect(InquiryVO inquiryVO) throws DataAccessException {
	
		return inquiryDAO.updateReflect(inquiryVO);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int deleteInquiry(int ef_i_no) throws DataAccessException {
		
		return inquiryDAO.deleteInquiry(ef_i_no);	
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 추가하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int addComment(InquiryCommentVO inquiryCommentVO) throws DataAccessException {
		System.out.println("addComment serviceVO ==>" + inquiryCommentVO);
		return inquiryDAO.addComment(inquiryCommentVO);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 추가
	//-----------------------------------------------------------------------------------------------------------	
	@Override
	public int addContact(InquiryVO inquiryVO) throws DataAccessException {
		System.out.println("addContact Service ==> " + inquiryVO);
		return inquiryDAO.addContact(inquiryVO);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 이미지 파일 명 imgInquiry ==> 안겹치게 하기 위해서
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int imgContact(String ef_imageFileName) throws DataAccessException {
		
		return inquiryDAO.imgContact(ef_imageFileName);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 삭제하기
	//-----------------------------------------------------------------------------------------------------------	
	@Override
	public int deleteComment(int ef_comment) throws DataAccessException {
		
		return inquiryDAO.deleteComment(ef_comment);
	}
	
	
	
	
} // End - public class InquiryServiceImpl implements InquiryService
