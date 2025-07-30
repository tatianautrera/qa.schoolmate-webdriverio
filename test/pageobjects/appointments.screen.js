import { $ } from '@wdio/globals'
import Page from './screen.js';


class AppointmentsScreen extends Page {

    get txtAgenda() {
        return $('android=new UiSelector().text("Agenda").instance(0)')
    }

    get btnMoreOptions() {
        return $('android=new UiSelector().description("More options")')
    }

    get btnFinishSession() {
        return $('android=new UiSelector().text("Finalizar sessão")')
    }

    get btnNewActivity() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_create_job")')
    }

    get selectDate() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_date_delivery")')
    }

    get btnSaveActivity() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_save_job")')
    }

    get btnApply() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_apply")')
    }

    get inputObservartions() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tiet_observation")')
    }

    get txtDateAppointment() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_dt_delivery")')
    }

    get txtTypeActivity() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/cv_type_job")')
    }

    get btnOptionsActivity() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/iv_arrow")')
    }

    get btnDeleteActivity() {
        return $('android=new UiSelector().className("android.view.ViewGroup").instance(4)')
    }

    get btnCloseModal() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/iv_close")')
    }

    get btnPermission() {
        return $('android=new UiSelector().resourceId("com.android.permissioncontroller:id/permission_allow_button")')
    }

    get checkBoxActivity() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/cb_finish_job")')
    }

    get btnEditActivity() {
        return $('android=new UiSelector().className("android.view.ViewGroup").instance(3)')
    }

    selectDateEditActivity(text) {
        return $(`android=new UiSelector().text("${text}")`)
    }

    async finishSession() {
        await this.btnMoreOptions.click()
        await this.btnFinishSession.click()
    }

    async registerActivity(activity) {
        await this.btnNewActivity.click()
        if (activity.discipline !== undefined) {
            await this.accessElementByText('Disciplina').click()
            await this.accessElementByText(activity.discipline).click()
        }
        if (activity.type !== undefined) {
            await this.accessElementByText('Tipo de atividade').click()
            await this.accessElementByText(activity.type).click()
        }
        if (activity.date !== undefined) {
            await this.selectDate.click()
            await this.selectData(await this.getDayCurrent(activity.date)).click()
            await this.btnApply.click()
        }
        await this.accessElementByText('Observações').setValue(activity.observations)
        await this.btnSaveActivity.click()
    }

    async assertActivityCreated(activity) {
        await this.assertText(this.titleScreen, `${activity.type} de ${activity.discipline}`)
        await this.assertText(this.txtDateAppointment, `Para ${activity.date}`)
        await this.assertText(this.txtTypeActivity, activity.type)
    }

    async deleteActivity() {
        await this.btnOptionsActivity.click()
        await this.btnDeleteActivity.click()
        await this.btnYes.click()
    }

    async editActivity(activityCurrent, activityNew) {
        await this.btnOptionsActivity.click()
        await this.btnEditActivity.click()
        if (activityNew.field === 'discipline') {
            await this.accessElementByText(activityCurrent.discipline).click()
            await this.accessElementByText(activityNew.discipline).click()
        } else if (activityNew.field === 'type') {
            await this.accessElementByText(activityCurrent.type).click()
            await this.accessElementByText(activityNew.type).click()
        } else if (activityNew.field === 'observations') {
            await this.inputObservartions.setValue(activityNew.observations)
        } else if (activityNew.field === 'date') {
            await this.selectDate.click()
            await this.selectDateEditActivity(await this.getDayCurrent(activityNew.date)).click()
            await this.btnApply.click()
        }else if (activityNew.field === 'all') {
            await this.accessElementByText(activityCurrent.discipline).click()
            await this.accessElementByText(activityNew.discipline).click()
            await this.accessElementByText(activityCurrent.type).click()
            await this.accessElementByText(activityNew.type).click()
            await this.inputObservartions.setValue(activityNew.observations)
            await this.selectDate.click()
            await this.selectDateEditActivity(await this.getDayCurrent(activityNew.date)).click()
            await this.btnApply.click()
        }
        await this.btnSaveActivity.click()
    }

    async closeModal() {
        await this.btnCloseModal.click()
    }

    async openModalActivity() {
        await this.btnNewActivity.click()
    }

    async checkActivity() {
        await this.checkBoxActivity.click()
    }
}
export default new AppointmentsScreen();
