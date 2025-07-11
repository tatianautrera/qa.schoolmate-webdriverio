import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json'

describe('Create Activity test', () => {
    beforeEach(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        await AppointmentsScreen.acceptAlert()
    })
    it('When fill valid datas, Should create a activity', async () => {
        let activity = {
            discipline: 'Cypress automação web',
            type: 'Avaliação',
            date: await AppointmentsScreen.getDataCurrent(),
            observations: 'teste'
        }
        await AppointmentsScreen.registerActivity(activity)
        await AppointmentsScreen.assertActivityCreated(activity)
        await AppointmentsScreen.deleteActivity()
    })

    it.only('When not fill required fields, Should not create a activity', async () => {
        let activity = {
            type: 'Avaliação',
            date: await AppointmentsScreen.getDataCurrent(),
            observations: 'teste'
        }
        await AppointmentsScreen.registerActivity(activity)
        //await expect(AppointmentsScreen.btnSaveActivity).toBeVisible()
        await AppointmentsScreen.assertNotEnabled(AppointmentsScreen.btnSaveActivity)
    })
})