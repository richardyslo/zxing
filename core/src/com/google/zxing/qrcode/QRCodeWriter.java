/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.encoder.ByteArray;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Hashtable;

/**
 * This object renders a QR Code as a ByteMatrix 2D array of greyscale values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class QRCodeWriter implements Writer {

  public ByteMatrix encode(byte[] contents, BarcodeFormat format, int width, int height)
      throws WriterException {

    return encode(contents, format, width, height, null);
  }

  public ByteMatrix encode(byte[] contents, BarcodeFormat format, int width, int height,
      Hashtable hints) throws WriterException {

    if (contents == null || contents.length == 0) {
      throw new IllegalArgumentException("Found empty contents");
    }

    if (format != BarcodeFormat.QR_CODE) {
      throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
    }

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Requested dimensions are too small: " + width + "x" +
          height);
    }

    // TODO: Check hints for error correction level instead of hardcoding
    int errorCorrectionLevel = QRCode.EC_LEVEL_L;
    QRCode code = new QRCode();
    if (Encoder.Encode(new ByteArray(contents), errorCorrectionLevel, code)) {
      return renderResult(code, width, height);
    } else {
      throw new WriterException("Could not generate a QR Code");
    }
  }

  // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
  // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).
  private ByteMatrix renderResult(QRCode code, final int width, final int height) {
    ByteMatrix input = code.matrix();
    int inputWidth = input.width();
    int inputHeight = input.height();
    int outputWidth = Math.max(width, inputWidth);
    int outputHeight = Math.max(height, inputHeight);

    int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);
    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
    int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

    ByteMatrix output = new ByteMatrix(outputHeight, outputWidth);
    byte[][] outputArray = output.getArray();

    // We could be tricky and use the first row in each set of multiple as the temporary storage,
    // instead of allocating this separate array.
    byte[] row = new byte[outputWidth];

    // 1. Write the white lines at the top
    for (int y = 0; y < topPadding; y++) {
      setRowColor(outputArray[y], (byte) 255);
    }

    // 2. Expand the QR image to the multiple
    final byte[][] inputArray = input.getArray();
    for (int y = 0; y < inputHeight; y++) {
      // a. Write the white pixels at the left of each row
      for (int x = 0; x < leftPadding; x++) {
        row[x] = (byte) 255;
      }

      // b. Write the contents of this row of the barcode
      int offset = leftPadding;
      for (int x = 0; x < inputWidth; x++) {
        byte value = (inputArray[y][x] == 1) ? 0 : (byte) 255;
        for (int z = 0; z < multiple; z++) {
          row[offset + z] = value;
        }
        offset += multiple;
      }

      // c. Write the white pixels at the right of each row
      offset = leftPadding + (inputWidth * multiple);
      for (int x = offset; x < outputWidth; x++) {
        row[x] = (byte) 255;
      }

      // d. Write the completed row multiple times
      offset = topPadding + (y * multiple);
      for (int z = 0; z < multiple; z++) {
        System.arraycopy(row, 0, outputArray[offset + z], 0, outputWidth);
      }
    }

    // 3. Write the white lines at the bottom
    int offset = topPadding + (inputHeight * multiple);
    for (int y = offset; y < outputHeight; y++) {
      setRowColor(outputArray[y], (byte) 255);
    }

    return output;
  }

  private static void setRowColor(byte[] row, byte value) {
    for (int x = 0; x < row.length; x++) {
      row[x] = value;
    }
  }

}
