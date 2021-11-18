package fancycar.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fancycar.model.*;
import fancycar.dal.ReviewsDao;

@WebServlet("/findreviewsbyvin")
public class FindReviewsByVin extends HttpServlet {

    protected ReviewsDao reviewsDao;

    @Override
    public void init() throws ServletException {
        reviewsDao = ReviewsDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<Reviews> reviews = new ArrayList<Reviews>();

        String vin = req.getParameter("vin");
        if (vin == null || vin.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            try {
                reviews = reviewsDao.getReviewsByVin(vin);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying results for " + vin);
            messages.put("previousvin", vin);
        }
        req.setAttribute("reviews", reviews);

        req.getRequestDispatcher("/FindReviewsByVin.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<Reviews> reviews = new ArrayList<Reviews>();

        String vin = req.getParameter("vin");
        if (vin == null || vin.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            try {
                reviews = reviewsDao.getReviewsByVin(vin);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
            messages.put("success", "Displaying results for " + vin);
        }
        req.setAttribute("reviews", reviews);

        req.getRequestDispatcher("/FindReviewsByVin.jsp").forward(req, resp);
    }
}
