package com.shoppeo.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppeo.admin.FileUploadUtil;
import com.shoppeo.common.entity.Role;
import com.shoppeo.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model, "id","desc", null);
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model, 
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword ) {
		
//		System.out.println("분류필드 : "+ sortField);
//		System.out.println("분류기준 : "+ sortDir);
		
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1 ;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1 ;
		
//		System.out.println("현재 페이지 : "+pageNum);
//		System.out.println("전체 개수 : "+page.getTotalElements());
//		System.out.println("전체 페이지 수 : "+page.getTotalPages());
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		model.addAttribute("keyword", keyword);
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
 		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		
		model.addAttribute("pageTitle", "회원 생성 페이지");
		model.addAttribute("clickLabel", "생성");
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
//		System.out.println(user);
//		System.out.println("확인"+multipartFile.getOriginalFilename());
		String message = "";
		boolean isUpdatingUser = (user.getId() != null);
		if(isUpdatingUser){
			message ="회원 수정에 성공하였습니다.";
		}else {
			message ="회원 생성에 성공하였습니다.";
		}
		
		
		if(!multipartFile.isEmpty()) {   // 사진파일이 비워져있지 않다면 ( 이미 존재하는 계정 )
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveduser = service.save(user);
			
			String uploadDir = "user-photos/" + saveduser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			// 사진을 넣지 않은 경우 ( 가입할 때부터 사진이 없거나, 신규 )
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		redirectAttributes.addFlashAttribute("success_message", message);
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
		User user = service.get(id);
		List<Role> listRoles = service.listRoles();
		
		model.addAttribute("user",user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "회원 수정 페이지");
		model.addAttribute("clickLabel", "수정");
		
		return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("not_found_message", ex.getMessage());
			return "redirect:/users";
		}
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
		service.delete(id);
		redirectAttributes.addFlashAttribute("success_message", "회원 삭제가 완료되었습니다.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("not_found_message", ex.getMessage());	
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name="id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, Model model) {
		service.updateUserEnabledStatus(id, enabled);
		redirectAttributes.addFlashAttribute("success_message", "상태가 변경되었습니다.");
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
		
	}
	
}
