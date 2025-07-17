import CreateUserScreen from '../pageobjects/createUser.screen.js'
import LoginScreen from '../pageobjects/login.screen.js'
import AppointmentsScreen from '../pageobjects/appointments.screen.js';
import { faker } from '@faker-js/faker';
import login from '../data/login/login.json'

describe('Login test', () => {
    beforeEach(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
    })
    it('When fill valid login, Should open the app', async () => {
        await LoginScreen.login(login.ValidLogin[0])
        if(await AppointmentsScreen.btnPermission.isDisplayed()){
             await LoginScreen.acceptAlert()
        } 
        await LoginScreen.assertEnabled(AppointmentsScreen.txtAgenda)
        await AppointmentsScreen.finishSession()
    })
    it('When fill login not active, Should return a message error not login the app', async () => {
        var user = {
            name: "Tatiana Utrera",
            email: faker.internet.username() + "teste1@teste.com",
            password: "123456",
            confirmPassword: "123456"
        }

        await CreateUserScreen.openCreateUser()
        await CreateUserScreen.createUser(user)
        await LoginScreen.login(user)
        await LoginScreen.assertText((LoginScreen.toastTitleSuccess), "Email não verificado")
        await LoginScreen.assertText((LoginScreen.toastMessage), "Por favor, verifique seu email antes de fazer login.")
    })
    it('When fill incorret password, Should return a message error not login the app', async () => {
        await LoginScreen.login(login.InvalidPassword[0])
        await LoginScreen.assertText((LoginScreen.toastTitleSuccess), "Erro no login")
        await LoginScreen.assertText((LoginScreen.toastMessage), "Credenciais inválidas")
    })
    it('When fill invalid login, Should return a message error not login the app', async () => {
        await LoginScreen.login(login.InvalidLogin[0])
        await LoginScreen.assertText((LoginScreen.toastTitleSuccess), "Erro no login")
        await LoginScreen.assertText((LoginScreen.toastMessage), "Credenciais inválidas")
    })
    it('When fill valid login and check de option save login, Should login and save the credencials', async () => {
        await LoginScreen.login(login.ValidLoginWithSaveLogin[0])
        if(await AppointmentsScreen.btnPermission.isDisplayed()) 
            await LoginScreen.acceptAlert()
        await LoginScreen.assertEnabled(AppointmentsScreen.txtAgenda)
        await AppointmentsScreen.finishSession()
        await LoginScreen.assertSaveLogin(login.ValidLoginWithSaveLogin[0])
    })
    it('When click the option "Esqueci minha senha", Should open de screen forgot password', async () => {
        await LoginScreen.openForgotPasswordScreen()
        await LoginScreen.assertText(LoginScreen.titleScreen, "Recuperar senha") 
    })
})