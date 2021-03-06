using System.Collections.Generic;

/*
 * Copyright 2009 ZXing authors
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

namespace com.google.zxing.multi
{

	using BinaryBitmap = com.google.zxing.BinaryBitmap;
	using DecodeHintType = com.google.zxing.DecodeHintType;
	using NotFoundException = com.google.zxing.NotFoundException;
	using Result = com.google.zxing.Result;


	/// <summary>
	/// Implementation of this interface attempt to read several barcodes from one image.
	/// </summary>
	/// <seealso cref= com.google.zxing.Reader
	/// @author Sean Owen </seealso>
	public interface MultipleBarcodeReader
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: com.google.zxing.Result[] decodeMultiple(com.google.zxing.BinaryBitmap image) throws com.google.zxing.NotFoundException;
	  Result[] decodeMultiple(BinaryBitmap image);

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: com.google.zxing.Result[] decodeMultiple(com.google.zxing.BinaryBitmap image, java.util.Map<com.google.zxing.DecodeHintType,?> hints) throws com.google.zxing.NotFoundException;
      Result[] decodeMultiple(BinaryBitmap image, IDictionary<DecodeHintType, object> hints);

	}

}