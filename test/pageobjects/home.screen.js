import { $ } from '@wdio/globals'
import Page from './screen.js';


class HomeScreen extends Page {
    
get txtAgenda(){
    return $('android=new UiSelector().text("Agenda").instance(0)')
}

get btnMoreOptions(){
    return $('android=new UiSelector().description("Mais opções")')
}

get btnFinishSession(){
    return $('android=new UiSelector().text("Finalizar sessão")')
}

async finishSession(){
    await this.btnMoreOptions.click()
    await this.btnFinishSession.click()
}

}

export default new HomeScreen();
