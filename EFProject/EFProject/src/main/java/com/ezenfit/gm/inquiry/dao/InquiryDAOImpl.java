package com.ezenfit.gm.inquiry.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ezenfit.gm.vo.InquiryCommentVO;
import com.ezenfit.gm.vo.InquiryVO;
import com.ezenfit.gm.vo.Criteria;

//-----------------------------------------------------------------------------------------------------------
// public class ArticleDAOImpl implements ArticleDAO
//-----------------------------------------------------------------------------------------------------------
@Repository("inquiryDAO")
public class InquiryDAOImpl implements InquiryDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(InquiryDAOImpl.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 목록 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------				
	@Override
	public List<InquiryVO> inquiryList(Criteria cri) throws DataAccessException {
		List<InquiryVO> inquiryList = sqlSession.selectList("mapper.inquiry.inquiryList", cri);		
		return inquiryList;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 리스트 카운트
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	public int listCount() throws DataAccessException {
		
		return sqlSession.selectOne("mapper.inquiry.listCount");
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 작성하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int addInquiry(InquiryVO inquiryVO) throws DataAccessException {
		System.out.println("daoVO ==>" + inquiryVO);
		 return sqlSession.insert("mapper.inquiry.addInquiry", inquiryVO); 
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 게시글현황 페이지 불러오기
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	public InquiryVO inqSituation(int ef_i_no) throws DataAccessException {
		System.out.println("daoVO ==>" + ef_i_no);
		
		return sqlSession.selectOne("mapper.inquiry.inqSituation", ef_i_no);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 현황 리스트 불러오기
	//-----------------------------------------------------------------------------------------------------------				
	@Override
	public List<InquiryCommentVO> commentList(int ef_i_no) throws DataAccessException {
		System.out.println("commentList daoVO ==>" + ef_i_no);
		
		List<InquiryCommentVO> commentList = sqlSession.selectList("mapper.inquiry.commentList", ef_i_no);		
		return commentList;
	}

	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 수정반영하기
	//-----------------------------------------------------------------------------------------------------------	
	@Override
	public int updateReflect(InquiryVO inquiryVO) throws DataAccessException {	
		System.out.println("updateReflect DAO ==> " + inquiryVO);
		return sqlSession.update("mapper.inquiry.updateReflect", inquiryVO);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의사항 삭제하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int deleteInquiry(int ef_i_no) throws DataAccessException {	
		System.out.println("deleteInquiry DAO ==> " + ef_i_no);
		return sqlSession.delete("mapper.inquiry.deleteInquiry", ef_i_no);
	}

	//-----------------------------------------------------------------------------------------------------------
	// 답글 추가하기
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int addComment(InquiryCommentVO inquiryCommentVO) throws DataAccessException {
		System.out.println("addComment daoVO  ==>" + inquiryCommentVO);
		return sqlSession.insert("mapper.inquiry.addComment", inquiryCommentVO); 
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 문의하기 추가
	//-----------------------------------------------------------------------------------------------------------	
	@Override
	public int addContact(InquiryVO inquiryVO) throws DataAccessException {
		System.out.println("inquiryVO DAO ==> " + inquiryVO);
		int result = sqlSession.insert("mapper.inquiry.addContact", inquiryVO);
		System.out.println("addContact DAO 결과 ==> " + result);
		return result;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 이미지 파일 명 imgInquiry ==> 안겹치게 하기 위해서
	//-----------------------------------------------------------------------------------------------------------
	@Override
	public int imgContact(String ef_imageFileName) throws DataAccessException {
		
		return sqlSession.selectOne("mapper.inquiry.imgContact", ef_imageFileName);
	}
	
	//-----------------------------------------------------------------------------------------------------------
	// 답글 삭제하기
	//-----------------------------------------------------------------------------------------------------------		
	@Override
	public int deleteComment(int ef_comment) throws DataAccessException {
		
		System.out.println("deleteComment DAO ==> " + ef_comment);
		return sqlSession.delete("mapper.inquiry.deleteComment", ef_comment);
	}
	
	

} // End - public class ArticleDAOImpl implements ArticleDAO
