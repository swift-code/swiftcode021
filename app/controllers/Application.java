package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forms.LoginForm;
import forms.SignUpForm;
import models.Profile;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by lubuntu on 8/21/16.
 */
public class Application extends Controller {
    @Inject
    FormFactory formFactory;

    @Inject
    ObjectMapper objectMapper;

    public Result signup()
    {
        Form<SignUpForm> signupform = formFactory.form(SignUpForm.class).bindFromRequest();
        if(signupform.hasErrors())
        {
            return ok(signupform.errorsAsJson());
        }

        Profile profile=new Profile(signupform.data().get("firstname"),signupform.data().get("lastname"));
        Profile.db().save(profile);

        User user= new User(signupform.data().get("email"),signupform.data().get("password"));
        user.profile=profile;
        User.db().save(user);

        return ok((JsonNode)objectMapper.valueToTree(user));
    }

    public Result login()
    {
        Form<LoginForm> loginform = formFactory.form(LoginForm.class).bindFromRequest();
        if(loginform.hasErrors())
        {
            return ok(loginform.errorsAsJson());
        }
        else
        {

            User user= new User(loginform.data().get("email"),loginform.data().get("password"));
            User.db().save(user);
            return ok((JsonNode)objectMapper.valueToTree(user));
        }

    }
}
