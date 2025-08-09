import DisciplineScreen from '../pageobjects/discipline.screen';
import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import discipline from '../data/discipline/discipline.json';

describe('Edit Discipline test', () => {
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

    it('When field valid data, Should edit discipline', async () => {
         for (const data of discipline.editDisciplineSuccess) {
            await DisciplineScreen.createDiscipline(discipline.createDisciplineSuccess)
            await DisciplineScreen.assertEnabled(await DisciplineScreen.accessElementByText(discipline.createDisciplineSuccess.name))
            await DisciplineScreen.editDiscipline(data, discipline.createDisciplineSuccess.name)
            await DisciplineScreen.assertEnabled(await DisciplineScreen.accessElementByText(data.name))
            await DisciplineScreen.deleteDiscipline(data.name)
        } 
    })

    it('When not fill required fields, Should not create discipline', async () => {
        await DisciplineScreen.createDiscipline(discipline.createDisciplineSuccess)
        await DisciplineScreen.assertEnabled(await DisciplineScreen.accessElementByText(discipline.createDisciplineSuccess.name))
        for (const data of discipline.InputWithInvalidFields) {
            await DisciplineScreen.accessEditDisciplineScreen(discipline.createDisciplineSuccess.name)
            await DisciplineScreen.fillFields(data)
            await DisciplineScreen.assertNotEnabled(await DisciplineScreen.btnSaveDiscipline)
            await DisciplineScreen.closeModal()
        }
        await DisciplineScreen.deleteDiscipline(discipline.createDisciplineSuccess.name)
    })
})