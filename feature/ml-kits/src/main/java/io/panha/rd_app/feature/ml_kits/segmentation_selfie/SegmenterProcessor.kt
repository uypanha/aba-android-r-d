package io.panha.rd_app.feature.ml_kits.segmentation_selfie

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.Segmenter
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import io.panha.rd_app.feature.ml_kits.GraphicOverlay
import io.panha.rd_app.feature.ml_kits.VisionProcessorBase

/** A processor to run Segmenter.  */
class SegmenterProcessor : VisionProcessorBase<SegmentationMask> {
    private val segmenter: Segmenter

    constructor(context: Context) : this(context, /* isStreamMode= */ true)

    constructor(
        context: Context,
        isStreamMode: Boolean
    ) : super(
        context
    ) {
        val optionsBuilder = SelfieSegmenterOptions.Builder()
        optionsBuilder.setDetectorMode(
            if (isStreamMode) SelfieSegmenterOptions.STREAM_MODE
            else SelfieSegmenterOptions.SINGLE_IMAGE_MODE
        )
//        if (PreferenceUtils.shouldSegmentationEnableRawSizeMask(context)) {
//            optionsBuilder.enableRawSizeMask()
//        }

        val options = optionsBuilder.build()
        segmenter = Segmentation.getClient(options)
        Log.d(TAG, "SegmenterProcessor created with option: " + options)
    }

    override fun detectInImage(image: InputImage): Task<SegmentationMask> {
        return segmenter.process(image)
    }

    override fun onSuccess(segmentationMask: SegmentationMask, graphicOverlay: GraphicOverlay, original: Bitmap?) {
        graphicOverlay.add(
            SegmentationGraphic(
                graphicOverlay,
                segmentationMask,
                original = original
            )
        )
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Segmentation failed: $e")
    }

    companion object {
        private const val TAG = "SegmenterProcessor"
    }
}
