import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import appointments from '../data/appointments/appointments.json';

describe('Create Activity test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await AppointmentsScreen.btnPermission.isDisplayed())
            await AppointmentsScreen.acceptAlert()
    })

    after(async () => {
        await AppointmentsScreen.finishSession();
    })

    it('When fill valid datas, Should create a activity', async () => {
        appointments.ValidActivity[0].date = await AppointmentsScreen.getDataCurrent()
        await AppointmentsScreen.registerActivity(appointments.ValidActivity[0])
        await AppointmentsScreen.assertActivityCreated(appointments.ValidActivity[0])
        await AppointmentsScreen.deleteActivity()
    })
    it('When not fill required fields, Should not create a activity', async () => {
        for (const activity of appointments.InputWithOutFields) {
            if (activity.date === "")
                activity.date = await AppointmentsScreen.getDataCurrent();
            await AppointmentsScreen.registerActivity(activity);
            await AppointmentsScreen.assertNotEnabled(AppointmentsScreen.btnSaveActivity);
            await AppointmentsScreen.closeModal();
        }
    })

    it('Close modal created activity', async () => {
        await AppointmentsScreen.openModalActivity();
        await AppointmentsScreen.closeModal();
        await AppointmentsScreen.assertEnabled(AppointmentsScreen.txtAgenda)
    })
})