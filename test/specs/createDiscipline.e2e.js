import DisciplineScreen from '../pageobjects/discipline.screen';
import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import discipline from '../data/discipline/discipline.json';

describe('Create Discipline test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await DisciplineScreen.btnPermission.isDisplayed())
            await DisciplineScreen.acceptAlert()
        await DisciplineScreen.accessDisciplineScreen()
    })

    after(async () => {
        await AppointmentsScreen.finishSession();
    })

    it('When click button create discipline, Should open create discipline modal', async () => {
        await DisciplineScreen.accessCreateDisciplineScreen()
        await DisciplineScreen.assertText(await DisciplineScreen.titleScreen, "Adicionar disciplina")
        await DisciplineScreen.closeModal()
    })

    it('When field valid data, Should create discipline', async () => {
        await DisciplineScreen.createDiscipline(discipline.createDisciplineSuccess)
        await DisciplineScreen.assertEnabled(await DisciplineScreen.accessElementByText(discipline.createDisciplineSuccess.name))
        await DisciplineScreen.deleteDiscipline(discipline.createDisciplineSuccess.name)
    })

    it('When not fill required fields, Should not create discipline', async () => {
        for (const data of discipline.InputWithInvalidFields) {
            await DisciplineScreen.fillFields(data)
            await DisciplineScreen.assertNotEnabled(await DisciplineScreen.btnSaveDiscipline)
            await DisciplineScreen.closeModal()

        }
    })
})