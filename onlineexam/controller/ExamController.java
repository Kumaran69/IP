package com.onlineexam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExamController {

    private final Map<Integer, String> correctAnswers = Map.of(
            1, "Delhi",
            2, "Java",
            3, "HyperText Markup Language"
    );

    @GetMapping("/")
    public String home() {
        return "redirect:/login";  // redirect to login page
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session, Model model) {
        if ("student".equals(username) && "1234".equals(password)) {
            session.setAttribute("user", username);
            return "redirect:/exam";
        }
        model.addAttribute("error", "Invalid login!");
        return "login";
    }

    @GetMapping("/exam")
    public String examPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Map<Integer, String> qn = Map.of(
                1, "Capital of India?",
                2, "Which language is platform independent?",
                3, "HTML stands for?"
        );

        Map<Integer, List<String>> options = new HashMap<>();
        options.put(1, List.of("Delhi", "Mumbai", "Kolkata"));
        options.put(2, List.of("C", "Java", "Python"));
        options.put(3, List.of("HyperText Markup Language",
                               "HighText Machine Language",
                               "HyperTabular Markup Language"));

        model.addAttribute("questions", qn);
        model.addAttribute("options", options);
        return "exam";
    }

    @PostMapping("/submit")
    public String submitExam(@RequestParam Map<String,String> params,
                             HttpSession session, Model model) {
        int score = 0;
        for (int id : correctAnswers.keySet()) {
            String ans = params.get("q" + id);
            if (correctAnswers.get(id).equals(ans)) score++;
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("score", score);
        model.addAttribute("total", correctAnswers.size());
        return "result";
    }
}
