package ru.freelib.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.Book;
import ru.freelib.service.BookService;

import java.io.*;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private BookService bookService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Book book = bookService.getById(id);

        if (book == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String uploadPath = req.getServletContext().getRealPath("/uploads");
        File file = new File(uploadPath, book.getFilePath());

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден.");
            return;
        }
        String encodedFileName = java.net.URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");

        resp.setContentType(getServletContext().getMimeType(file.getName()));
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

        try (InputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }
}
