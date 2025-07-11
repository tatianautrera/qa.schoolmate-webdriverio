import CreateUserScreen from '../pageobjects/createUser.screen.js'
import LoginScreen from '../pageobjects/login.screen.js'
import { faker } from '@faker-js/faker';
import user from '../data/createAccount/createUser.json'

describe('Create account test', () => {
    beforeEach(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await CreateUserScreen.openCreateUser()
    })
    it('When click the button "Criar conta" Should open the create account screen', async () => {
        await CreateUserScreen.assertText(CreateUserScreen.titlePage, 'Crie sua conta')
    })
    it('When Field valid datas Should register a account', async () => {
        var user = {
            name: "Tatiana Utrera",
            email: faker.internet.username() + "teste1@teste.com",
            password: "123456",
            confirmPassword: "123456"
        }
        await CreateUserScreen.createUser(user)
        await CreateUserScreen.assertText(LoginScreen.toastTitleSuccess, 'Usuário criado')
    })
    it('When not field required fields, Should not hability button created Account', async () => {
        for (let i = 0; i < user.InputWithOutFields.length; i++) {
            await CreateUserScreen.fillFieldsCreateUser(user.InputWithOutFields[i])
            await CreateUserScreen.assertNotEnabled(CreateUserScreen.btnSubmit)
        }
    })
    it('When field invalid field, Should not hability button created Account', async () => {
        for (let i = 0; i < user.InputWithInvalidFields.length; i++) {
            await CreateUserScreen.fillFieldsCreateUser(user.InputWithInvalidFields[i])
            await CreateUserScreen.assertText(CreateUserScreen.messageError(user.InputWithInvalidFields[i].field), user.InputWithInvalidFields[i].message)
            await CreateUserScreen.assertNotEnabled(CreateUserScreen.btnSubmit)
        }
    })
    it('When field incorret confirm password, Should not created Account', async () => {
        await CreateUserScreen.createUser(user.InputWithInvalidConfirmPassword)
        await CreateUserScreen.assertText((CreateUserScreen.toastTitleSuccess), "Erro de validação")
        await CreateUserScreen.assertText((CreateUserScreen.toastMessage), user.InputWithInvalidConfirmPassword.message)
    })

    it('When field incorret confirm password, Should not created Account', async () => {
        await CreateUserScreen.createUser(user.InputWithInvalidConfirmPassword)
        await CreateUserScreen.assertText((CreateUserScreen.toastTitleSuccess), "Erro de validação")
        await CreateUserScreen.assertText((CreateUserScreen.toastMessage), user.InputWithInvalidConfirmPassword.message)
    })

    it('When Field email already use, Should not register a account', async () => {
        var user = {
            name: "Tatiana Utrera",
            email: faker.internet.username() + "teste1@teste.com",
            password: "123456",
            confirmPassword: "123456"
        }
        await CreateUserScreen.createUser(user)
        await CreateUserScreen.openCreateUser()
        await CreateUserScreen.createUser(user)
        await CreateUserScreen.assertText((CreateUserScreen.toastTitleSuccess), "Erro ao criar usuário")
        await CreateUserScreen.assertText((CreateUserScreen.toastMessage), "Este e-mail já está cadastrado. Tente outro e-mail.")
    })
})