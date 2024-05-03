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

    public LocalDate getFirstDayForSensor(String sensorId) {
        List<SensorData> sensorDataList = amplitudeRepository.findSensorDataBySensorId(sensorId);
        if (sensorDataList.isEmpty()) {
            return null;
        }
        String firstDateString = sensorDataList.get(0).getDate();
        return LocalDate.parse(firstDateString);
    }

    public LocalDate getSecondDayForSensor(String sensorId) {
        List<SensorData> sensorDataList = amplitudeRepository.findSensorDataBySensorId(sensorId);
        if (sensorDataList.size() < 2) {
            return null; // Retourner null si la liste ne contient pas assez de données
        }
        String secondDateString = sensorDataList.get(1).getDate();
        return LocalDate.parse(secondDateString);
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
