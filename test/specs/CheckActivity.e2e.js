import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import appointments from '../data/appointments/appointments.json';

describe('Check Activity test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await AppointmentsScreen.btnPermission.isDisplayed())
            await AppointmentsScreen.acceptAlert()
        appointments.ValidActivity[0].date = await AppointmentsScreen.getDataCurrent()
        await AppointmentsScreen.registerActivity(appointments.ValidActivity[0])
    })

    after(async () => {
        await AppointmentsScreen.deleteActivity()
    })
    it('When check a activity, Should closed the activity', async () => {
        await AppointmentsScreen.checkActivity()
        
    })
})