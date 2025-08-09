import DisciplineScreen from '../pageobjects/discipline.screen';
import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import discipline from '../data/discipline/discipline.json';

describe('Delete Discipline test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await DisciplineScreen.btnPermission.isDisplayed())
            await DisciplineScreen.acceptAlert()
        await DisciplineScreen.accessDisciplineScreen()
    })

    beforeEach(async () => {
        await DisciplineScreen.createDiscipline(discipline.createDisciplineSuccess)
        await DisciplineScreen.assertEnabled(await DisciplineScreen.accessElementByText(discipline.createDisciplineSuccess.name))
    })

    after(async () => {
        await AppointmentsScreen.finishSession();
    })

    it('When confirm delete, Should delete discipline', async () => {
        await DisciplineScreen.deleteDiscipline(discipline.createDisciplineSuccess.name)
        await DisciplineScreen.elementNotDisplayed(await DisciplineScreen.accessElementByText(discipline.createDisciplineSuccess.name))
    })

    it('When access modal delete discipline, Should open delete modal discipline', async () => {
        await DisciplineScreen.acessModaldeleteDiscipline(discipline.createDisciplineSuccess.name)
        await AppointmentsScreen.assertText(await AppointmentsScreen.messageScreen,`Realmente deseja excluir a disciplina ${discipline.createDisciplineSuccess.name}?`)
        await DisciplineScreen.btnYes.click()
    })
})