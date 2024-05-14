package com.codeverce.aquasonicbackend.Service;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.AmplitudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmplitudeService {

    @Autowired
    private AmplitudeRepository amplitudeRepository;

    public LocalDate getAmplitudes(String sensorId,int numberOfDay) {
        List<SensorData> sensorDataList = amplitudeRepository.findSensorDataBySensorId(sensorId);
        if (sensorDataList.isEmpty()) {
            return null;
        }
        String firstDateString = sensorDataList.get(numberOfDay-1).getDate();
        return LocalDate.parse(firstDateString);
    }

    public List<double[]> getAmplitudesForSensorOnDay(String sensorId, LocalDate day) {
        List<SensorData> sensorDataList = amplitudeRepository.findSensorDataBySensorId(sensorId);
        List<SensorData> dataForDay = sensorDataList.stream()
                .filter(data -> LocalDate.parse(data.getDate()).isEqual(day))
                .collect(Collectors.toList());
        return dataForDay.stream()
                .map(SensorData::getAmplitudes)
                .collect(Collectors.toList());
    }
}
