/*
 * Copyright 2010 sasc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uz.uzkassa.apay.data.repository.qr.card.engine.javaemreader;

/**
 *
 * @author sasc
 */
public interface Tag {

    public enum Class{
        UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE
    }

    boolean isConstructed();

    byte[] getTagBytes();

    String getName();

    String getDescription();

    TagType getType();

    TagValueType getTagValueType();

    Class getTagClass();

    int getNumTagBytes();

}


