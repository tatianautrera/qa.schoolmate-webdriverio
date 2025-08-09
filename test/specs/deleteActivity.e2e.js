import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import appointments from '../data/appointments/appointments.json';
import editAppointment from '../data/appointments/editAppointment.json';

describe('Delete Activity test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await AppointmentsScreen.btnPermission.isDisplayed())
            await AppointmentsScreen.acceptAlert()
        appointments.ValidActivity[0].date = await AppointmentsScreen.getNextData(1)
        await expect(AppointmentsScreen.btnNewActivity).toBeDisplayed()
        await AppointmentsScreen.registerActivity(appointments.ValidActivity[0])
    })

    after(async () => {
        await AppointmentsScreen.finishSession();
    })

    it('When access button delete a activity, Should open delete activity modal', async () => {
        await AppointmentsScreen.openDeleteActivityModal() 
        await AppointmentsScreen.assertText(await AppointmentsScreen.messageScreen,"Realmente deseja excluir a atividade Avaliação de Cypress automação web?")
        await AppointmentsScreen.closeModal()
    })

    it('When delete a activity, Should delete the activity', async () => {
       await AppointmentsScreen.deleteActivity() 
       await AppointmentsScreen.assertWithoutActivity()
    })
})