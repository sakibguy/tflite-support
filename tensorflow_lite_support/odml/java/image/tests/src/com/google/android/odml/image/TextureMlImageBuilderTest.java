/* Copyright 2021 Google LLC. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.google.android.odml.image;

import static com.google.common.truth.Truth.assertThat;

import android.opengl.EGLContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

/** Tests for {@link TextureMlImageBuilder} */
@RunWith(RobolectricTestRunner.class)
public final class TextureMlImageBuilderTest {
  public static final int TEXTURE_NAME = 2;
  public static final int NATIVE_CONTEXT = 5;
  public static final int IMAGE_HEIGHT = 200;
  public static final int IMAGE_WIDTH = 100;
  public static final int IMAGE_FORMAT = MlImage.IMAGE_FORMAT_RGBA;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private EGLContext eglContext;

  @Test
  public void build_fromBitmap_succeeds() {
    MlImage image =
        new TextureMlImageBuilder(
                TEXTURE_NAME, eglContext, IMAGE_WIDTH, IMAGE_HEIGHT, MlImage.IMAGE_FORMAT_RGBA)
            .setNativeContext(NATIVE_CONTEXT)
            .build();
    ImageContainer container = image.getContainer();

    assertThat(image.getWidth()).isEqualTo(IMAGE_WIDTH);
    assertThat(image.getHeight()).isEqualTo(IMAGE_HEIGHT);
    assertThat(image.getContainedImageProperties())
        .containsExactly(
            ImageProperties.builder()
                .setStorageType(MlImage.STORAGE_TYPE_TEXTURE)
                .setImageFormat(IMAGE_FORMAT)
                .build());

    TextureFrame textureFrame = ((TextureImageContainer) container).getTextureFrame();
    assertThat(textureFrame.getTextureName()).isEqualTo(TEXTURE_NAME);
    assertThat(textureFrame.getEglContext()).isSameInstanceAs(eglContext);
    assertThat(textureFrame.getNativeContext()).isEqualTo(NATIVE_CONTEXT);
  }
}
