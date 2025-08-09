import { $ } from '@wdio/globals'
import Page from './screen.js';


class DisciplineScreen extends Page {

    get btnDiscipline() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_discipline")')
    }

    get btnAccessCreateDisciplineModal() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_create_discipline")')
    }

    get inputDiscipline() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_discipline")')
    }

    get inputTeacher() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_teacher")')
    }

    get btnSaveDiscipline() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_save_discipline")')
    }

    btnOptionsDisicipline(text) {
        return $(`//android.widget.TextView[@resource-id="com.fsacchi.schoolmate:id/tv_discipline_name" and @text="${text}"]/../android.widget.ImageView[@resource-id="com.fsacchi.schoolmate:id/iv_arrow"]`)
    }

    get btnDeleteDiscipline() {
        return $('android=new UiSelector().text("Excluir")')
    }

    async accessDisciplineScreen() {
        await this.btnDiscipline.click()
    }

    async accessCreateDisciplineScreen() {
        await this.btnAccessCreateDisciplineModal.click()
    }

    async fillFields(discipline) {
        await this.accessCreateDisciplineScreen()
        await this.inputDiscipline.setValue(discipline.name)
        await this.inputTeacher.setValue(discipline.teacher)
    }

    async createDiscipline(discipline) {
        await this.fillFields(discipline)
        await this.btnSaveDiscipline.click()
    }

    async deleteDiscipline(name) {
        await this.btnOptionsDisicipline(name).click()
        await this.btnDeleteDiscipline.click()
        await this.btnYes.click()
    }
}

export default new DisciplineScreen();