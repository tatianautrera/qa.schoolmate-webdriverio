import { expect } from '@wdio/globals'
import CreateUserScreen from '../pageobjects/createUser.screen.js'
import LoginScreen from '../pageobjects/login.screen.js'
import HomeScreen from '../pageobjects/home.screen.js';
import { faker } from '@faker-js/faker';
import login from '../data/login/login.json'

describe('Login test', () => {
    beforeEach(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
    })
    it('When fill valid login, Should open the app', async () => {
        await LoginScreen.login(login.ValidLogin[0])
        await LoginScreen.acceptAlert()
        await LoginScreen.assertDisplayed(HomeScreen.txtAgenda)
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
    it.only('When fill valid login and check de option save login, Should login and save the credencials', async () => {
        await LoginScreen.login(login.ValidLoginWithSaveLogin[0])
        await LoginScreen.acceptAlert()
        await LoginScreen.assertDisplayed(HomeScreen.txtAgenda)
        await HomeScreen.finishSession()
        await LoginScreen.validValueField(LoginScreen.inputEmail,login.ValidLoginWithSaveLogin[0].email)
    })

    it.only('When click the option "Esqueci minha senha", Should open de screen forgot password', async () => {
        await LoginScreen.linkForgotPassword()
        
    })
})