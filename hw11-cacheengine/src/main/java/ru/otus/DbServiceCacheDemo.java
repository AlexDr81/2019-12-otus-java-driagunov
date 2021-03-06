package ru.otus;

import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.dbservice.DBServiceUser;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.service.DbServiceUserCacheImpl;

import java.util.List;


public class DbServiceCacheDemo {


  public static void main(String[] args) {

    Class<?>[] annotatedClasses = {User.class, Address.class, Phone.class};
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", annotatedClasses);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    DBServiceUser dbServiceUser = new DbServiceUserCacheImpl(userDao);


    User vasia = createUser("Вася Пупкин", "МОСКВА", "+72390423094", "+5324343433");
    User lusia = createUser("Люся Педалькина", "ЕКАТЕРИНБУРГ", "+79890238256", "+743382752930");

    long id1 = dbServiceUser.saveUser(vasia);
    long id2 = dbServiceUser.saveUser(lusia);
    System.out.println("================================================");

    System.out.println(dbServiceUser.getUser(id1));
    System.out.println(dbServiceUser.getUser(id1));
    System.out.println(dbServiceUser.getUser(id1));


    System.out.println(dbServiceUser.getUser(id2));
    System.out.println(dbServiceUser.getUser(id2));
    System.out.println(dbServiceUser.getUser(id2));

  }

  private static User createUser(String name, String address, String phone1, String phone2) {
    User user = new User();
    user.setName(name);
    Address addressUser = new Address(address, user);
    Phone phone1User = new Phone(phone1, user);
    Phone phone2User = new Phone(phone2, user);
    user.setAddress(addressUser);
    user.setPhones(List.of(phone1User, phone2User));
    return user;
  }


}
