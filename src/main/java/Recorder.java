import javafx.application.Platform;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;

class Recorder implements Runnable {
    private Controller controller;
    private static ArrayList<Mixer.Info> mixersList = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
    private double[] buffer;
    private int N = 1024;
    private TargetDataLine line = null;
    private AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

    Recorder(Controller fx) {
        controller = fx;
    }

    @Override
    public void run() {
        System.out.println(mixersList);
        while (true) {
            try {
                line = AudioSystem.getTargetDataLine(format, mixersList.get(3));
                line.open(format, N);
                System.out.println("Line opened.");
            } catch (LineUnavailableException e) {
                System.out.println("Error! LineUnavailable.");
                e.printStackTrace();
            }
            double maxFreq = 0;
            double lastPeak = 0;
            double lastRms = 0;
            double maxRms = 0;
            line.start();
            {
                System.out.println("Line starting...");
                double rms;
                double peak;
                float[] samples = new float[N];
                buffer = new double[N / 2];
                byte[] buf = new byte[N * 2];
                int b;
                while (true) {
                    b = N * 2;
                       line.read(buf, 0, buf.length);
                        try {
                            float fade = controller.getFade();
                            if (!format.isBigEndian()) {
                                for (int i = 0; i < b - 1; i += 2) {
                                    int sample = 0;
                                    sample |= buf[i + 1] << 8; //
                                    sample |= buf[i] & 0xff;
                                    samples[i / 2] = (sample / 32768f);
                                }
                            } else {
                                for (int i = 0; i < b - 1; i += 2) {
                                    int sample = 0;
                                    sample |= buf[i] << 8;
                                    sample |= buf[i + 1] & 0xff;//
                                    samples[i / 2] = (sample / 32768f);
                                }
                            }
                            int i = 0;
                            peak = 0;
                            rms = 0;
                            for (float sample : samples) {
                                //filling fftBuffer
                                i++;
                                double abs = Math.abs(sample);
                                if (abs > peak) {
                                    peak = abs;
                                }
                                rms += sample * sample;
                            }
                            //setting METERS
                            rms = (float) Math.sqrt(rms / samples.length);
                            if (rms > maxRms) {
                                maxRms = rms;
                            }
                            if (lastRms > rms) {
                                rms = lastRms * (fade);
                            }
                            if (lastPeak > peak) {
//                                System.out.println("Peak: " + peak);
                                peak = lastPeak * (fade) * fade;

                            }
                            if (rms < maxRms) {
                                maxRms -= (1 - fade);
                                lastRms = maxRms;
                            } else {
                                lastRms = rms;
                            }
                            lastPeak = peak;

                            double finalPeak = peak;
                            double finalRms = rms;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    controller.setPeak(finalPeak);
                                    controller.setRms(finalRms);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }
}

