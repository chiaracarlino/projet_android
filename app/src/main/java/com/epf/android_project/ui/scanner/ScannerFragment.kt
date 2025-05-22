package com.epf.android_project.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.epf.android_project.R
import com.epf.android_project.utils.Resource
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.bumptech.glide.Glide

class ScannerFragment : Fragment(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView
    private val viewModel: ScannerViewModel by viewModels()

    private lateinit var loader: ProgressBar
    private lateinit var productLayout: LinearLayout
    private lateinit var productImage: ImageView
    private lateinit var productTitle: TextView
    private lateinit var productPrice: TextView
    private lateinit var errorMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_scanner, container, false)

        scannerView = root.findViewById(R.id.scanner_view)
        loader = root.findViewById(R.id.progress_bar)
        productLayout = root.findViewById(R.id.product_layout)
        productImage = root.findViewById(R.id.product_image)
        productTitle = root.findViewById(R.id.product_title)
        productPrice = root.findViewById(R.id.product_price)
        errorMessage = root.findViewById(R.id.error_message)

        return root
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
        requestCameraPermission()

        viewModel.scannedProduct.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    loader.visibility = View.VISIBLE
                    productLayout.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }

                is Resource.Success -> {
                    loader.visibility = View.GONE
                    productLayout.visibility = View.VISIBLE
                    errorMessage.visibility = View.GONE

                    val product = result.data
                    productTitle.text = product.title
                    productPrice.text = "${product.price} €"
                    Glide.with(this).load(product.image).into(productImage)
                }

                is Resource.Error -> {
                    loader.visibility = View.GONE
                    productLayout.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    errorMessage.text = result.message
                }

                else -> Unit
            }
        }
    }

    override fun handleResult(rawResult: Result?) {
        rawResult?.text?.let { scannedText ->
            viewModel.processScannedQRCode(scannedText)
        }
        // scannerView.resumeCameraPreview(this) // À activer si tu veux scanner en boucle
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 1)
        }
    }
}
