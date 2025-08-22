import DisciplineScreen from '../pageobjects/discipline.screen';
import AppointmentsScreen from '../pageobjects/appointments.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import appointments from '../data/appointments/appointments.json';

describe('List activities per discipline test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await DisciplineScreen.btnPermission.isDisplayed())
            await DisciplineScreen.acceptAlert()
            appointments.ValidActivity[0].date = await AppointmentsScreen.getNextData(1)
            await AppointmentsScreen.registerActivity(appointments.ValidActivity[0])
        await DisciplineScreen.accessDisciplineScreen()
    })

    beforeEach(async () => {
        await DisciplineScreen.accessAppointmentsScreen()
        await DisciplineScreen.accessDisciplineScreen()
    })

    after(async () => {
        await DisciplineScreen.accessAppointmentsScreen()
        await AppointmentsScreen.deleteActivity() 
        await AppointmentsScreen.finishSession();
    })
    
    it('Given have an activity for discipline, When access activity, Should return all axtivities for discipline', async () => {
        await DisciplineScreen.ListActivitiesDiscipline(appointments.ValidActivity[0].discipline)
        await AppointmentsScreen.assertActivityCreated(appointments.ValidActivity[0])
    })

    it('Given not have an activity for discipline When access activiy, Should return empty list', async () => {
        await DisciplineScreen.ListActivitiesDiscipline("Webdriver io web")
        await DisciplineScreen.assertMessage("Nenhuma atividade para essa disciplina")
    })
})