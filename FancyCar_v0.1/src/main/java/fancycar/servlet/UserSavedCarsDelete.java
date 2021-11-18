package fancycar.servlet;

import fancycar.dal.CarListingsDao;
import fancycar.dal.UserSavedCarsDao;
import fancycar.dal.UsersDao;
import fancycar.model.CarListings;
import fancycar.model.UserSavedCars;
import fancycar.model.Users;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/usersavedcarsdelete")
public class UserSavedCarsDelete extends HttpServlet {
    protected UserSavedCarsDao userSavedCarsDao;
    @Override
    public void init() throws ServletException {
        userSavedCarsDao = UserSavedCarsDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        // Provide a title and render the JSP.
        messages.put("title", "Delete Saved Car");
        req.getRequestDispatcher("/UserSavedCarsDelete.jsp").forward(req, resp);
    }



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String username = req.getParameter("username");
        String vin = req.getParameter("vin");
        if (vin == null || vin.trim().isEmpty() || username == null || username.trim().isEmpty()) {
            messages.put("title", "Invalid vin" + vin);
            messages.put("disableSubmit", "true");
        } else {
            UsersDao usersDao = UsersDao.getInstance();
            CarListingsDao carListingsDao = CarListingsDao.getInstance();
            Users user = new Users(username);
            CarListings car = new CarListings(vin);
            try {
                user = usersDao.getUserByUserName(username);
                car = carListingsDao.getCarListingByVin(vin);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            UserSavedCars userSavedCar = new UserSavedCars(user, car);
            try {
                userSavedCar = userSavedCarsDao.delete(userSavedCar);
                if (userSavedCar == null) {
                    messages.put("title", "Successfully deleted " + vin);
                    messages.put("disableSubmit", "true");
                } else {
                    messages.put("title", "Failed to delete " + vin);
                    messages.put("disableSubmit", "false");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        req.getRequestDispatcher("/UserSavedCarsDelete.jsp").forward(req, resp);
    }
}
