package com.epf.android_project.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.epf.android_project.databinding.FragmentScannerBinding
import com.epf.android_project.utils.Resource
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private val scannerViewModel: ScannerViewModel by viewModels()


    private val CAMERA_PERMISSION_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasCameraPermission()) {
            startScanning()
        } else {
            requestCameraPermission()
        }

        //pour tester le qrcode
        binding.fakeScanButton.setOnClickListener {
            val fakeQrCode = "3"
            scannerViewModel.processScannedQRCode(fakeQrCode)
        }
        scannerViewModel.scannedProduct.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Success -> {
                    resource.data?.let { product ->
                        val action = ScannerFragmentDirections.actionScannerFragmentToProductFragment(product.id)
                        findNavController().navigate(action)
                    } ?: run {
                        Toast.makeText(requireContext(), "Produit introuvable", Toast.LENGTH_LONG).show()
                        binding.barcodeScanner.resume()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                    binding.barcodeScanner.resume()
                }
                is Resource.Loading -> {}
                null -> {}
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                val scannedText = it.text
                binding.barcodeScanner.pause()
                scannerViewModel.processScannedQRCode(scannedText)
            }
        }

        override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
        }
    }

    private fun startScanning() {
        binding.barcodeScanner.decodeContinuous(callback)
        binding.barcodeScanner.resume()
    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            binding.barcodeScanner.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
