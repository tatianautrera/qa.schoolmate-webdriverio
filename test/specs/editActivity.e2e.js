import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import appointments from '../data/appointments/appointments.json';
import editAppointment from '../data/appointments/editAppointment.json';

describe('Edit Activity test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await AppointmentsScreen.btnPermission.isDisplayed())
            await AppointmentsScreen.acceptAlert()
        appointments.ValidActivity[0].date = await AppointmentsScreen.getNextData(1)
    })

    /*after(async () => {
        await AppointmentsScreen.finishSession();
    })*/
    it('When edit a activity, Should edited the activity', async () => {
        for (const activity of editAppointment.EditActivities) {
            await expect(AppointmentsScreen.btnNewActivity).toBeDisplayed()
            await AppointmentsScreen.registerActivity(appointments.ValidActivity[0])
            activity.date = await appointments.ValidActivity[0].date;
            if (activity.field === 'date' || activity.field === 'all')
                activity.date = await AppointmentsScreen.getNextData(2);
            await AppointmentsScreen.editActivity(appointments.ValidActivity[0], activity)
            await AppointmentsScreen.assertActivityCreated(activity)
            await AppointmentsScreen.deleteActivity()
        }
    })
})