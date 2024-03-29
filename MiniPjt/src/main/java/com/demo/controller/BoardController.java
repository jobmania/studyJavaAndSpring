package com.demo.controller;

import com.demo.beans.BoardInfoBean;
import com.demo.beans.ContentBean;
import com.demo.beans.LoginUserBean;
import com.demo.beans.PageBean;
import com.demo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;

	@Resource(name = "loginUserBean")
	private LoginUserBean loginUserBean;

	@Autowired
	public BoardController(BoardService boardService){
		this.boardService = boardService;
	}

	@GetMapping("/main")
	public String main(@RequestParam("board_info_idx") int board_info_idx, Model model,
						@RequestParam(value = "page", defaultValue = "1") int page) {

		model.addAttribute("board_info_idx", board_info_idx);

		String boardInfoName = boardService.getBoardInfoName(board_info_idx);
		model.addAttribute("boardInfoName", boardInfoName);

		List<ContentBean> contentList = boardService.getContentList(board_info_idx, page);
		model.addAttribute("contentList", contentList);

		PageBean pageBean = boardService.

				getContentCnt(board_info_idx, page);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("page", page);



		return "board/main";
	}

	@GetMapping("/read")
	public String read(@RequestParam("board_info_idx") int board_info_idx,
					   @RequestParam("content_idx") int content_idx,
					   @RequestParam(value = "page", defaultValue = "1") int page,
					   Model model) {
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);
		model.addAttribute("loginUserBean", loginUserBean);

		ContentBean contentInfo = boardService.getContentInfo(content_idx);

		model.addAttribute("readContentBean", contentInfo);
		model.addAttribute("page", page);

		return "board/read";
	}

	@GetMapping("/write")
	public String write(@ModelAttribute("writeContentBean")ContentBean writeContentBean,
						@RequestParam("board_info_idx") int board_info_idx ) {
		writeContentBean.setContent_board_idx(board_info_idx);
		return "board/write";
	}

	@PostMapping("write_pro")
	public String write_pro(@Valid @ModelAttribute("writeContentBean")ContentBean writeContentBean, BindingResult result){
		if(result.hasErrors()) {
			return "board/write";
		}

		boardService.addContentInfo(writeContentBean);

		return "board/write_success";
	}

	@GetMapping("/not_writer")
	public String not_writer(){
		return "board/not_writer";
	}



	@GetMapping("/modify")
	public String modify(@RequestParam("board_info_idx")int board_info_idx,
						 @RequestParam("content_idx")int content_idx,
						 @ModelAttribute("modifyContentBean") ContentBean modifyContentBean,
						 @RequestParam("page") int page,
						 Model model) {

		modifyContentBean.setContent_board_idx(board_info_idx);
		modifyContentBean.setContent_idx(content_idx);

		boardService.getContents(modifyContentBean);
		model.addAttribute("modifyContentBean", modifyContentBean);
		model.addAttribute("page", page);

		return "board/modify";
	}

	@PostMapping("/modify_pro")
	public String modify_pro(@Valid @ModelAttribute("modifyContentBean") ContentBean modifyContentBean,
							 BindingResult result,
							 @RequestParam("page") int page,
							 Model model) {
		model.addAttribute("page", page);
		if(result.hasErrors()) {
			return "board/modify";
		}

		boardService.modifyContentInfo(modifyContentBean);

		return "board/modify_success";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("board_info_idx")int board_info_idx,
						 @RequestParam("content_idx")int content_idx, Model model) {

		boardService.deleteContentInfo(content_idx);
		model.addAttribute("board_info_idx", board_info_idx);

		return "board/delete";
	}

}
