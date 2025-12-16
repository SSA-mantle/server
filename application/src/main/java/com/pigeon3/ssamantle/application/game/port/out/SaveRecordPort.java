package com.pigeon3.ssamantle.application.game.port.out;

import com.pigeon3.ssamantle.domain.model.record.Record;

public interface SaveRecordPort {
    Record save(Record record);
}
