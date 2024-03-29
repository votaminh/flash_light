package com.flash.light.domain.usecase

import com.flash.light.R
import com.flash.light.domain.layer.SoundModel
import javax.inject.Inject

class GetListSoundUseCase @Inject constructor() :
    UseCase<GetListSoundUseCase.Param, List<SoundModel>>() {

    open class Param() : UseCase.Param()

    override suspend fun execute(param: Param): List<SoundModel> = listOf(
        SoundModel(R.drawable.img_police, R.string.txt_police, R.raw.sound_police),
        SoundModel(R.drawable.img_doorbell, R.string.txt_doorbell, R.raw.sound_doorbell),
        SoundModel(R.drawable.img_hello, R.string.txt_hello, R.raw.sound_hello),
        SoundModel(R.drawable.img_harp, R.string.txt_harp, R.raw.sound_harp),
        SoundModel(R.drawable.img_laughing, R.string.txt_laughing, R.raw.sound_laughing),
        SoundModel(R.drawable.img_alarm_clock, R.string.txt_alarm_clock, R.raw.sound_alarm_clock),
        SoundModel(R.drawable.img_rooster, R.string.txt_rooster, R.raw.sound_rooster),
        SoundModel(R.drawable.img_piano, R.string.txt_piano, R.raw.sound_piano),
        SoundModel(R.drawable.img_sneeze, R.string.txt_sneeze, R.raw.sound_sneeze),
        SoundModel(R.drawable.img_train, R.string.txt_train, R.raw.sound_train),
        SoundModel(R.drawable.img_wind_chimes, R.string.txt_wind_chimes, R.raw.sound_wind_chimes),
        SoundModel(R.drawable.img_whistle, R.string.txt_whistle, R.raw.sound_whistle)
    )
}