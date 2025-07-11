import CreateUserScreen from '../pageobjects/createUser.screen.js'
import ForgotPasswordScreen from '../pageobjects/forgotPassword.screen.js';
import login from '../data/login/login.json'

describe('Forgot password test', () => {
    beforeEach(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
    })
    it('When fill valid email, Should send email for recouver password', async () => {
        await ForgotPasswordScreen.recoverPassword(login.ValidLogin[0].email)
        await ForgotPasswordScreen.assertText(ForgotPasswordScreen.titleScreen, "Acessar conta")
        await ForgotPasswordScreen.assertText((ForgotPasswordScreen.toastTitleSuccess), "Email enviado")
        await ForgotPasswordScreen.assertText((ForgotPasswordScreen.toastMessage), "Altere sua senha através do email, para acessar o Schoolmate")
    })
    it('When fill invalid email, Should return a message error not recovere password', async () => {
        await ForgotPasswordScreen.recoverPassword("fsdfsdfsdfsd.com")
        await ForgotPasswordScreen.assertText(CreateUserScreen.messageError("email"), "E-mail inválido")
        await ForgotPasswordScreen.assertNotEnabled(ForgotPasswordScreen.btnSendLink)
    })
})