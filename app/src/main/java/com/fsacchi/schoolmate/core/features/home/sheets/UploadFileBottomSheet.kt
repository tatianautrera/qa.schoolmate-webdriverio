package com.fsacchi.schoolmate.core.features.home.sheets

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.addVerticalDivider
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.toArrayList
import com.fsacchi.schoolmate.core.extensions.toast
import com.fsacchi.schoolmate.core.extensions.visible
import com.fsacchi.schoolmate.core.features.home.OptionItem
import com.fsacchi.schoolmate.core.platform.adapter.BaseAdapter
import com.fsacchi.schoolmate.core.platform.adapter.BaseDiffCallback
import com.fsacchi.schoolmate.data.model.file.FileModel
import com.fsacchi.schoolmate.databinding.BottomSheetUploadFileBinding
import com.fsacchi.schoolmate.databinding.ItemOptionsBinding
import com.fsacchi.schoolmate.databinding.OptionsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage

class UploadFileBottomSheet : BottomSheetDialogFragment() {

    private lateinit var fileExtension: String

    private val selectFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            handleFileSelection(it)
        } ?: run {
            toast("Selecione um arquivo para envio", Toast.LENGTH_LONG)
        }
    }

    private var listener: (FileModel) -> Unit = {}
    private lateinit var binding: BottomSheetUploadFileBinding

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, saved: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_upload_file, root, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showChooseFile()
        insertListener()
    }

    private fun insertListener() {
        binding.ivClose.clickListener{
            dismiss()
        }

        binding.clChooseFile.clickListener {
            openFileSelector()
        }
    }

    private fun openFileSelector() {
        selectFileLauncher.launch("*/*")
    }

    private fun handleFileSelection(uri: Uri) {
        val contentResolver = requireContext().contentResolver

        val fileName = getFileName(uri) ?: "unknown"
        fileExtension = getFileExtension(fileName)

        val mimeType = contentResolver.getType(uri)
        val validMimeTypes = listOf(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        )

        if (mimeType !in validMimeTypes) {
            toast("Formato de arquivo não suportado", Toast.LENGTH_LONG)
            return
        }

        val fileSize = contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
        if (fileSize > MAX_SIZE_FILE) {
            toast("Arquivo excede o limite de 10MB", Toast.LENGTH_LONG)
            return
        }

        showLoading()
        uploadToFirebase(uri)
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast('.', "unknown")
    }

    private fun showChooseFile() {
        binding.clAwaitFile.visible(false)
        binding.clErrorFile.visible(false)
        binding.clChooseFile.visible(true)
    }

    private fun showLoading() {
        binding.clAwaitFile.visible(true)
        binding.clErrorFile.visible(false)
        binding.clChooseFile.visible(false)
    }

    private fun showError(errorMessage: String) {
        binding.clAwaitFile.visible(false)
        binding.clErrorFile.visible(true)
        binding.clErrorFile.rootView.findViewById<TextView>(R.id.tx_error_message).text = errorMessage
        binding.clErrorFile.rootView.findViewById<MaterialButton>(R.id.btn_try_again).clickListener {
            showChooseFile()
        }
        binding.clChooseFile.visible(false)
    }

    private fun uploadToFirebase(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        val fileName = "${System.currentTimeMillis()}_${uri.lastPathSegment}"
        val fileRef = storageReference.child("uploads/$fileName.${fileExtension}")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { url ->
                    val nameFile = uri.lastPathSegment ?: "unknown"
                    val fileModel = FileModel(
                        nameFile = nameFile,
                        extension = fileExtension,
                        urlFirebase = url.toString()
                    )
                    dismiss()
                   listener.invoke(fileModel)
                }

            }
            .addOnFailureListener {
                showError(it.message ?: "Arquivo inválido ou muito grande")
            }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(javaClass.simpleName) == null) {
            show(manager, javaClass.simpleName)
        }
    }

    fun setListener(listener: (FileModel) -> Unit) = apply {
        this.listener = listener
    }

    companion object {

        const val MAX_SIZE_FILE = 10 * 1024 * 1024
        fun newInstance() = UploadFileBottomSheet()
    }
}
