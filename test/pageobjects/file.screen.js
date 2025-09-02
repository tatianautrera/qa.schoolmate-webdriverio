import { $ } from '@wdio/globals'
import Page from './screen.js';
import path from 'path';
import { fileURLToPath } from 'url'; // Importe esta função

class FileScreen extends Page {

    get btnFile() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_file")')
    }

    get btnUploadFile() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_create_file")')
    }

    get inputFileName() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tx_chooose_your_file")')
    }

    get inputSelectImage() {
        return $('android=new UiSelector().resourceId("com.google.android.documentsui:id/icon_thumb")')
    }

    get txtLabel() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tx_label_file")')
    }

    get inputTitle() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_title")')
    }

    get inputDiscipline() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_discipline")')
    }

    get inputObservation() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tiet_observation")')
    }

    get btnSaveUpload() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_save_file")')
    }

    get txtNameFile() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_name_file")')
    }

    get txtObservation() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_description_job")')
    }

    btnOptionsFile(text) {
        return $(`//android.widget.TextView[@resource-id="com.fsacchi.schoolmate:id/tv_name_file" and @text="${text}"]/../android.widget.ImageView[@resource-id="com.fsacchi.schoolmate:id/iv_arrow"]`)
    }

    async accessFileScreen() {
        await this.btnFile.click()
    }

    async accessUploadScreen() {
        await this.btnUploadFile.click()
    }

    async uploadNewFile(file) {
        await this.accessUploadScreen()
        await this.attachFile()
        await this.assertMessage("Você adicionou uma imagem")
        await this.inputTitle.setValue(file.title)
        if (file.Discipline != "") {
            await this.inputDiscipline.click()
            await this.accessElementByText(file.Discipline).click()
        }
        await this.inputObservation.setValue(file.observation)
        await this.btnSaveUpload.click()
    }

    async attachFile() {
        /* const currentDir = path.dirname(fileURLToPath(import.meta.url));
         const localFilePath = path.join(currentDir, '../data/files/image.png');
         await driver.uploadFile(localFilePath);*/
        await this.inputFileName.click();
        await this.inputSelectImage.click();
    }

    async deleteFile(title) {
        await this.openModalDeleteFile(title)
        await this.btnYes.click()
    }

    async openModalDeleteFile(title) {
        await this.btnOptionsFile(title).click()
        await this.accessElementByText("Excluir").click()
    }

    async EditFile(file, newFile) {
        await this.btnOptionsFile(file.title).click()
        await this.accessElementByText("Editar").click()
        await this.inputTitle.setValue(newFile.title)
        if (file.Discipline != "") {
            await this.inputDiscipline.click()
            await this.accessElementByText(newFile.Discipline).click()
        }
        await this.inputObservation.setValue(newFile.observation)
        await this.btnSaveUpload.click()
    }

    async assertMessage(message) {
        await expect(this.txtLabel).toHaveText(message)
    }

    async assertNewFile(file) {
        await expect(await this.txtNameFile).toHaveText(file.title)
        await this.assertEnabled(await this.accessElementByText(file.Discipline))
        await expect(this.txtObservation).toHaveText(file.observation)
    }
}

export default new FileScreen();