package com.group7.MGMN.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group7.MGMN.model.MarketVO;
import com.group7.MGMN.model.PagingVO;
import com.group7.MGMN.service.MarketService;

@Controller
public class MarketController {
	
	@Autowired
	MarketService service;
	
	// 중고거래 페이지로 이동
	@RequestMapping("/market")
	public String viewMarket() {
		return "market/marketView";
		
	}
	
	//지역별 상품 조회
	//@RequestMapping("/market/listRegionPost/{mkRegion}")
	//public String listRegionPost(@PathVariable String mkRegion, Model model) {
	//	ArrayList<MarketVO> mkList = service.listRegionPost(mkRegion);
	//	model.addAttribute("mkList", mkList);
		
	//	return "market/listRegionPostView";
	//}
	
	//지역별 상품 조회(페이징 추가)
	@RequestMapping("/market/listRegionPost/{mkRegion}")
	public String listRegionPostPaging(PagingVO vo, Model model
			, @RequestParam(value="nowPage", required=false)String nowPage
			, @RequestParam(value="cntPerPage", required=false)String cntPerPage) {
		int total = service.countMkBoard();
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "8";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) {
			cntPerPage = "8";
		}
		System.out.println("total : "+total);
		vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", vo);
		model.addAttribute("mkList", service.selectMkBoard(vo));
		return "market/listRegionPostView";
		
	}
	
	//상품 상세 정보 페이지로 이동
	@RequestMapping("/market/detailViewPost/{mkNo}")
	public String detailViewPost(@PathVariable int mkNo, Model model) {
		MarketVO mkVO = service.detailViewPost(mkNo);
		
		model.addAttribute("mkVO", mkVO);
		
		return "market/detailViewPost";
	}
	
	// 상품 등록 페이지로 이동
	@RequestMapping("/insertPostView")
	public String insertPostView() {
		return "market/insertPostView";
	}
	
	//상품 등록
	@RequestMapping("/insertPost")
	public String insertPost(MarketVO mkVO) {								// 전체 vo
		System.out.println(mkVO.getMkContent());								
		// mkVO.getMkContent() 값을 변수에 저장하고 substring 함수 사용해서 img 태그를 추출 추출한값을 mkVO.setMKImg 저장 >> 값을 리스트에서 출력!
		int idx1 = mkVO.getMkContent().indexOf("<img");
		int idx2 = mkVO.getMkContent().indexOf("\">");
		String imgThumbnail = mkVO.getMkContent().substring(idx1, idx2+2);
		mkVO.setMkImg(imgThumbnail);
		service.insertPost(mkVO);						// DB에 들어감
		return "redirect:/market/listRegionPost/" + mkVO.getMkRegion();		// vo에 넣어둔 mkRegion 객체를 불러오는 개념
	}
	
	// 수정 화면으로 이동
	@RequestMapping("/market/updatePostForm/{mkNo}")
	public String updatePostForm(@PathVariable int mkNo, Model model) {
		// 게시번호 전달하고, 해당 상품 정보 받아오기
		MarketVO mkVO = service.detailViewPost(mkNo);
		model.addAttribute("mkVO", mkVO);
		return "market/updatePostForm";
	}
	
	// 게시글 수정
	@RequestMapping("/market/updatePost")
	public String updatePost(MarketVO mkVO) {
		System.out.println("updatPost : " + mkVO);
		service.updatePost(mkVO);
		return "redirect:/market/listRegionPost/" + mkVO.getMkRegion();
	}
	
	// 게시글 삭제
	@RequestMapping("/market/deletePost/{mkNo}/{mkRegion}")
	public String deletePost(@PathVariable int mkNo, @PathVariable int mkRegion) {			// 주소로 값을 넘겨받은 경우에 @PathVariable로 받아준다
		service.deletePost(mkNo);
		return "redirect:/market/listRegionPost/" + mkRegion;
	}
	
}













