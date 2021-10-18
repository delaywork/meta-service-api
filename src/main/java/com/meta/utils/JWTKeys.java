package com.meta.utils;

import com.meta.model.RandomKeyResponse;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.*;

/**
 * @Author Martin
 */
public class JWTKeys {

    /**
     * 获取指定的Key
     * */
    public static Key getKey(String key){
        byte[] a = {15, 51, -124, -27, -58, -100, 88, -43, -108, -51, 84, -116, 24, 8, 5, -101, -109, -61, -100, 63, -47, -20, -118, 109, -57, 75, 10, -66, 32, -70, 90, -106, 31, 30, -115, -114, 12, 71, -55, -25, 66, 106, 56, -94, -95, 6, -92, -124, -36, 52, -30, -76, -20, 96, 26, 113, -127, -119, -45, -109, 106, -10, -72, 77};
        byte[] b = {47, 111, 48, 105, -83, 122, 107, 24, 33, -96, 58, 113, 96, -57, 97, 6, 30, -111, -109, -57, -44, 121, 1, -31, 40, -85, 91, 35, -84, 33, 3, 124, -64, -76, -31, 75, -115, 42, 6, 79, -50, 124, 102, -38, -66, 28, -44, -107, 18, 16, 39, 21, -117, 62, -59, -124, 18, -36, 55, -67, -66, -84, -128, -25};
        byte[] c = {116, 14, 68, 107, 63, -26, 61, 4, -22, 34, -51, -58, -49, 79, 90, 18, 1, -89, -20, -111, -50, -106, -62, -71, -28, -47, 84, 78, -40, -8, 78, -92, -71, -26, 82, 48, 38, 26, -128, -96, 70, -65, 29, -53, -27, 5, 35, -124, 7, 107, 79, 83, -22, -17, 88, 27, 14, 107, 69, 36, 123, -115, 122, -99};
        byte[] d = {80, 27, -25, 29, -24, 18, -126, -2, 54, 34, 79, 18, 65, 11, -58, 26, 38, 41, 10, 85, -76, -42, -114, -96, -14, 99, 94, -96, 73, -72, -125, -87, 103, 25, 25, 124, 104, 43, -62, 46, -59, -114, 102, -69, -24, 35, -11, -117, 49, -116, 1, -102, 56, -70, -104, 34, 61, 20, -4, -43, 34, -6, -93, 87};
        byte[] e = {-33, 93, 98, 28, 97, 25, 95, -42, 28, -60, -20, 88, -39, 37, 96, 36, -118, -22, 56, 1, 18, 12, -107, -6, 36, 98, 10, 43, -118, -15, -24, -19, -1, 113, -13, 50, -84, -47, 115, 20, -68, 124, 67, 68, -82, 23, -2, 100, 65, -108, 74, 53, -70, -14, 50, -86, 42, 106, -120, 50, 25, -26, -63, -8};
        byte[] f = {-59, -97, 87, 48, -21, 55, -70, 86, 122, 115, 19, 74, 26, -19, -41, -30, 8, -72, -13, 82, 34, -119, -52, 123, 61, -50, 50, -13, -39, 8, 31, -106, -13, 37, 49, 43, 64, 5, -70, 122, 0, -119, 120, 65, -81, -110, -110, 87, -28, 126, 37, 0, -6, -97, 57, -101, -118, -51, -90, -38, -98, 93, 100, 127};
        byte[] g = {-112, -27, 79, -125, -13, -102, -34, 108, 102, 23, -82, 45, -113, 81, 108, 15, 68, -84, -115, 88, 90, 115, -46, 126, 5, 96, -46, -63, -72, 54, -19, 99, -72, 90, -74, -102, 63, -103, -16, 40, 30, -75, 27, 15, 103, -71, 98, 85, -27, 60, 12, 15, -31, -49, -118, -32, 14, 125, -88, -42, 122, 66, 127, 85};
        byte[] h = {-99, -13, 97, 112, 1, -46, 38, -82, 38, -11, 63, -127, -126, -92, -13, -6, 78, -15, -33, -61, -4, -48, 107, -48, 40, -61, -4, 95, 4, 84, -128, -17, -49, -124, -39, 50, -122, 126, -5, -50, 87, 8, -45, 99, 89, -103, -35, -77, -99, -40, 105, 47, -99, 100, -88, -5, -14, -104, -12, 20, 127, -56, 111, 30};
        byte[] i = {64, -112, -76, 121, 115, 20, -104, 9, -83, 64, 79, 114, -62, -85, 113, 18, -123, 7, -35, -70, 109, 123, -63, 91, 29, 28, 56, 6, 80, -47, -57, 63, 106, -74, 34, -8, -5, -9, 55, -74, 115, -65, 3, 85, -2, 18, 55, 59, -16, 36, 84, -26, 84, 15, -95, 75, -59, -71, -102, 22, 119, -43, -16, 101};
        byte[] j = {18, 29, -99, -98, -66, 14, 72, -96, -47, -117, -73, -16, -46, -48, 12, 68, -68, -75, 14, 6, 48, -6, 59, -45, -117, 53, 99, -23, 4, -110, -34, -76, -33, 85, 70, -125, -41, -82, -105, -106, 41, -41, 66, -110, 33, 14, 36, -52, 94, 4, -101, 24, -45, -107, -34, -59, -84, 69, 13, -56, -51, 21, 80, -45};
        byte[] k = {-14, -67, 25, -78, 127, -121, 29, 44, -126, 17, -38, 104, 10, 46, -58, -17, 32, 93, 12, -126, -100, 25, -112, 40, 65, 103, 93, 63, 22, -22, 43, -5, 70, 62, 64, 53, 84, 20, -36, -109, -19, -119, 91, 95, 72, 34, 34, 51, -25, 95, -56, -75, -12, -11, 112, -11, -4, 37, 61, 32, -73, -120, 8, 84};
        byte[] l = {46, 16, 4, -77, -107, 7, -113, -41, 24, 66, 18, 45, 30, -86, -37, 51, 38, 23, -5, 56, 112, 73, -59, -124, -123, -37, 88, 8, 26, -123, -66, 72, 30, -123, -14, 11, 97, 38, -86, -126, 18, 77, 105, -1, 81, -97, -69, -21, 44, -120, 16, 0, 89, -50, 57, 91, 52, -91, 97, -71, 66, -37, 102, 79};
        byte[] m = {41, 95, 103, 7, 41, -26, 114, 125, -115, 42, -6, -33, 116, -97, 83, 13, -67, 55, -118, 98, -17, -70, -3, -31, -72, 18, 32, 42, -40, -17, -15, -72, -116, 0, 15, -109, -58, -77, 57, 14, -90, 110, 1, -21, 63, -29, -49, -118, 72, 8, 82, -67, 5, -34, -36, 9, -8, 45, 61, -108, 24, 57, 40, 118};
        byte[] n = {8, 53, 23, -101, 51, 71, 98, 52, 108, 83, 49, 104, -56, 33, 58, 118, 86, 66, 108, -110, 42, 127, -57, -8, 73, 126, 5, -63, -59, -128, 39, -97, -100, 28, -101, -29, -112, -91, 76, 57, -41, 118, 43, 18, -79, -51, 22, -15, -110, -61, -11, -109, -92, 63, -12, -63, 84, -115, 100, -56, -47, -116, 1, 49};
        byte[] o = {29, -9, -117, 80, 121, 6, 111, 68, 33, -104, -79, 49, -117, -62, -6, -106, 104, -97, -103, 91, -105, -49, 50, -70, 61, -1, 96, -13, 49, -112, -40, -33, 110, 8, 120, -12, -74, 91, -60, -106, -60, 58, -75, 8, -90, -66, 102, 46, 117, -110, -68, -105, 63, -40, -24, -15, 55, -44, -30, 103, -77, -40, -16, 87};
        byte[] p = {48, -33, 110, -77, 51, 7, 109, -79, 49, -80, -70, -109, -106, 28, 74, -22, 68, -127, -108, 102, 7, 11, -54, 74, 9, 125, 85, 38, -53, -35, -86, 53, -24, -106, 14, -2, -101, 42, 62, -78, 49, 54, -24, -60, -51, 55, -83, 42, -83, 50, 44, -2, -72, -93, 38, -20, 111, -86, -37, 31, 61, 33, 77, -36};
        byte[] q = {119, 17, -80, 77, -63, -48, -122, 14, 100, -71, -19, 108, 54, -11, -11, 77, 47, 77, 93, -55, 99, -91, -101, 63, -44, 17, 82, 89, -101, -76, 20, -47, -111, -111, -58, -76, -64, -5, -39, -59, 62, 4, -52, -86, 107, -31, -116, -28, 44, -113, 13, -12, -114, 28, -72, 8, -88, -78, -50, 45, 126, 109, -116, -6};
        byte[] r = {120, -107, -19, 112, -34, -5, 12, -98, 4, 92, -126, -44, -53, -14, -55, 15, -58, 119, -46, -95, 21, 56, 1, -122, 77, -76, 80, -55, -90, -118, -30, 121, 69, 95, 69, 124, -110, 40, 121, 114, -42, 95, 89, 124, -106, -15, 93, -34, 56, -26, -101, 94, 93, -123, 65, -16, 42, 10, -70, -15, 56, -16, -41, 46};
        byte[] s = {-37, -49, -75, 40, 123, 126, -113, -81, -21, -23, -3, -74, -89, -24, 23, -13, 74, -100, 94, -94, -65, -107, -32, 93, 22, -64, -105, -40, 20, 51, 31, -84, 127, 28, -125, -10, -65, 119, -43, 55, -36, -10, 97, -21, -43, -89, 79, -26, 117, -16, 15, -112, 64, -73, 121, 60, 12, -50, -126, 67, 110, 123, -64, -104};
        byte[] t = {-55, -7, 88, -35, 65, 107, -50, 57, 24, -43, -108, -21, -43, 1, -2, -10, -25, 42, 102, -119, 52, 0, 106, -104, -81, 88, -67, 114, -59, 116, 17, 9, -5, 8, 57, -116, -51, -119, 102, -45, 28, -14, -44, 72, -23, 102, 95, 106, 13, -44, 55, -85, 43, -64, 26, -110, 76, -128, -124, -46, 35, 89, -117, 87};
        byte[] u = {122, -53, -86, 112, 9, 4, -106, -117, -127, 56, 92, -48, -116, -120, 64, 53, -22, 14, -99, 34, -23, -81, -13, 64, 99, -116, 63, -34, 62, -50, 104, -76, -99, 99, -54, -114, -51, 46, 65, -124, 20, 29, -123, 25, 11, -85, -89, -87, 46, 39, -64, 65, -91, 41, -18, -20, 20, 44, -113, 44, -48, 15, -70, -81};
        byte[] v = {-4, -23, -27, -76, 41, -22, 66, -126, 11, -61, 40, -67, 12, 53, -30, 63, -66, -45, -4, 117, 5, -16, 82, 52, -68, -95, 51, -80, 69, -126, -83, 120, 94, 15, -71, -14, -44, -32, 119, -21, 95, -88, -109, 46, -97, -92, 109, -33, -92, -86, 52, -102, 68, -59, -34, 47, 97, -96, 23, 59, -18, -50, 46, 50};
        byte[] w = {71, -101, 32, -1, -121, -122, 78, 78, 120, -106, -116, -58, -68, 61, -82, -102, -12, -126, 89, 95, -35, 102, -34, 98, 67, 24, -62, -103, -126, -116, -2, -19, -86, -7, -34, -16, -48, 42, 72, -18, -105, 42, -6, -11, 96, -76, 77, 2, 20, -76, -107, -17, 53, 83, 43, -32, 101, 109, 16, 66, 30, 0, -28, -94};
        byte[] x = {74, 101, 49, 68, 108, 25, 6, 31, -16, -86, 1, 27, 54, 16, 120, 85, -74, -36, 93, 46, -34, 6, -34, 56, -73, -122, -17, 61, -49, 113, -105, 51, -36, 44, 5, 62, -71, 100, -85, -96, 80, 80, 5, 95, 43, 71, -77, 40, 110, 125, -37, -82, 121, -37, 19, -24, -44, 109, 126, 32, 110, -90, 37, 50};
        byte[] y = {-12, -124, -98, 70, 108, 58, 120, -7, -78, 39, 39, -84, 61, 82, 45, 17, 93, -84, -6, -15, -55, 55, -12, 19, 35, 107, 75, -46, -61, 107, 100, -99, -103, -34, 110, -101, 120, 36, 17, 9, 12, -123, 63, 102, -28, -118, -19, -60, -98, 117, 22, 84, 54, 10, -5, -75, 57, 41, 50, 76, 64, -74, -59, 47};
        byte[] z = {100, 110, 106, -23, -19, -121, 57, 6, -48, -119, -10, 9, 58, -11, -118, 85, -30, 71, 102, -97, -38, 92, 75, 48, -74, -1, 40, 110, 84, 87, 92, 112, -73, 110, -92, 26, -43, 123, 85, -27, 10, 98, 58, -99, 30, 11, -40, 73, 12, 41, -30, -31, -21, -123, -98, 59, -107, 92, 86, 48, 44, -43, -113, 73};
        byte[] A = {2, 1, -124, -27, -58, -100, 88, -43, -108, -51, 84, -116, 24, 98, 5, -101, -109, -61, -100, 63, -47, -20, -118, 109, -57, 75, 10, -66, 32, -70, 90, -106, 31, 30, -115, -114, 12, 71, -55, -25, 66, 106, 56, -94, -95, 6, -92, -124, -36, 52, -30, -76, -20, 96, 26, 113, -127, -119, -45, -109, 106, -10, -72, 77};
        byte[] B = {84, -116, 24, 98, -83, 122, 107, 24, 33, -96, 58, 113, 96, -57, 97, 66, 30, -111, -109, -57, -44, 121, 1, -31, 40, -85, 91, 35, -84, 33, 3, 124, -64, -76, -31, 75, -115, 42, 6, 79, -50, 124, 102, -38, -66, 28, -44, -107, 18, 16, 39, 21, -117, 62, -59, -124, 18, -36, 55, -67, -66, -84, -128, -25};
        byte[] C = {66, 30, 68, 107, 63, -26, 1, 4, -22, 34, -51, -58, -49, 79, 90, 18, 11, -89, -20, -111, -50, -106, -62, -71, -28, -47, 84, 78, -40, -8, 78, -92, -71, -26, 82, 48, 38, 26, -128, -96, 70, -65, 29, -53, -27, 5, 35, -124, 7, 107, 79, 83, -22, -17, 88, 27, 14, 107, 69, 36, 123, -115, 122, -99};
        byte[] D = {-85, 113, 118, -12, -24, 18, -126, -2, 54, 34, 79, 18, 65, 11, -58, 26, 38, 1, 100, 85, -76, -42, -114, -96, -14, 99, 94, -96, 73, -72, -125, -87, 103, 25, 25, 124, 104, 43, -62, 46, -59, -114, 102, -69, -24, 35, -11, -117, 49, -116, 1, -102, 56, -70, -104, 34, 61, 20, -4, -43, 34, -6, -93, 87};
        byte[] E = {-33, 93, 98, 28, 97, 25, 95, -42, 28, -60, -20, 88, -39, 37, 96, 36, -118, -22, 56, 1, 18, 12, -1, -6, 36, 98, 10, 43, -118, -15, -24, -19, -1, 113, -13, 50, -84, -47, 115, 20, -68, 124, 67, 68, -82, 23, -2, 100, 65, -108, 74, 53, -70, -14, 50, -86, 42, 106, -120, 50, 25, -26, -63, -8};
        byte[] F = {-106, 28, 74, -22, -21, 55, -70, 86, 122, 15, 19, 74, 26, -19, -41, -30, 8, -72, -123, 82, 34, -119, -52, 123, 61, -50, 50, -13, -39, 8, 31, -106, -13, 37, 49, 43, 64, 5, -70, 122, 0, -119, 120, 65, -81, -110, -110, 87, -28, 126, 37, 0, -6, -97, 57, -101, -118, -51, -90, -38, -98, 93, 100, 127};
        byte[] G = {-112, -27, 79, -1, -13, -102, -34, 108, 102, 23, -82, 45, -113, 81, 108, 115, 68, -84, -115, 88, 90, 115, -46, 126, 5, 96, -46, -63, -72, 54, -19, 99, -72, 90, -74, -102, 63, -103, -16, 40, 30, -75, 27, 15, 103, -71, 98, 85, -27, 60, 12, 15, -31, -49, -118, -32, 14, 125, -88, -42, 122, 66, 127, 85};
        byte[] H = {46, -58, -17, 112, 1, -46, 38, -82, 38, -11, 63, -27, -126, -92, -13, -65, 78, -15, -33, -61, -4, -48, 107, -48, 40, -61, -4, 95, 4, 84, -128, -17, -49, -124, -39, 50, -122, 126, -5, -50, 87, 8, -45, 99, 89, -103, -35, -77, -99, -40, 105, 47, -99, 100, -88, -5, -14, -104, -12, 20, 127, -56, 111, 30};
        byte[] I = {64, -112, -76, 121, 115, 20, -104, 9, -83, 64, 79, 114, -62, -85, 113, 118, -12, 7, -35, -70, 109, 123, -63, 91, 29, 28, 56, 6, 80, -47, -57, 63, 106, -74, 34, -8, -5, -9, 55, -74, 115, -65, 3, 85, -2, 18, 55, 59, -16, 36, 84, -26, 84, 15, -95, 75, -59, -71, -102, 22, 119, -43, -16, 101};
        byte[] J = {18, 29, -99, -98, -4, 14, 72, -96, -47, -117, -73, -16, -46, -48, 102, 68, -68, -75, 14, 6, 48, -6, 59, -45, -117, 53, 99, -23, 4, -110, -34, -76, -33, 85, 70, -125, -41, -82, -105, -106, 41, -41, 66, -110, 33, 14, 36, -52, 94, 4, -101, 24, -45, -107, -34, -59, -84, 69, 13, -56, -51, 21, 80, -45};
        byte[] K = {108, 115, 68, -78, 127, -121, 29, 44, -126, 17, -38, 104, 101, 46, -58, -17, 6, 93, 12, -126, -100, 25, -112, 40, 65, 103, 93, 63, 22, -22, 43, -5, 70, 62, 64, 53, 84, 20, -36, -109, -19, -119, 91, 95, 72, 34, 34, 51, -25, 95, -56, -75, -12, -11, 112, -11, -4, 37, 61, 32, -73, -120, 8, 84};
        byte[] L = {46, 16, 4, -7, -107, 7, -113, -41, 24, 66, 18, 45, 30, -86, -37, 51, 38, 23, -5, 56, 112, 73, -59, -124, -123, -37, 88, 8, 26, -123, -66, 72, 30, -123, -14, 11, 97, 38, -86, -126, 18, 77, 105, -1, 81, -97, -69, -21, 44, -120, 16, 0, 89, -50, 57, 91, 52, -91, 97, -71, 66, -37, 102, 79};
        byte[] M = {41, 95, 103, 7, 41, -26, 114, 125, -115, 42, -6, -33, 15, -97, 83, 123, -67, 55, -118, 98, -17, -70, -3, -31, -72, 18, 32, 42, -40, -17, -15, -72, -116, 0, 15, -109, -58, -77, 57, 14, -90, 110, 1, -21, 63, -29, -49, -118, 72, 8, 82, -67, 5, -34, -36, 9, -8, 45, 61, -108, 24, 57, 40, 118};
        byte[] N = {-6, -33, 15, -97, 71, 98, 52, 108, 83, 49, 104, -56, 33, 58, 118, 86, 66, 108, -110, 42, 127, -57, -8, 73, 126, 5, -63, -59, -128, 39, -97, -100, 28, -101, -29, -112, -91, 76, 57, -41, 118, 43, 18, -79, -51, 22, -15, -110, -61, -11, -109, -92, 63, -12, -63, 84, -115, 100, -56, -47, -116, 1, 49};
        byte[] O = {29, -9, -117, 80, 121, 6, 111, 68, 33, -104, -79, 49, -117, -62, -6, -106, 104, -97, -103, 87, -105, -49, 50, -70, 61, -1, 96, -13, 49, -112, -40, -33, 110, 8, 120, -12, -74, 91, -60, -106, -60, 58, -75, 8, -90, -66, 102, 46, 117, -110, -68, -105, 63, -40, -24, -15, 55, -44, -30, 103, -77, -40, -16, 87};
        byte[] P = {6, -33, 110, -77, 51, 7, 109, -79, 49, -87, -70, -109, -106, 28, 74, -22, 68, -127, -108, 102, 7, 11, -54, 74, 9, 125, 85, 38, -53, -35, -86, 53, -24, -106, 14, -2, -101, 42, 62, -78, 49, 54, -24, -60, -51, 55, -83, 42, -83, 50, 44, -2, -72, -93, 38, -20, 111, -86, -37, 31, 61, 33, 77, -36};
        byte[] Q = {124, 17, -80, 77, -63, -48, -12, 14, 100, -71, -19, 108, 54, -11, -11, 77, 47, 77, 93, -55, 99, -91, -101, 63, -44, 17, 82, 89, -101, -76, 20, -47, -111, -111, -58, -76, -64, -5, -39, -59, 62, 4, -52, -86, 107, -31, -116, -28, 44, -113, 13, -12, -114, 28, -72, 8, -88, -78, -50, 45, 126, 109, -116, -6};
        byte[] R = {-97, -107, -19, 1, -34, -5, 12, -98, 4, 92, -126, -44, -53, -14, -55, 15, -58, 119, -46, -95, 21, 56, 1, -122, 77, -76, 80, -55, -90, -118, -30, 121, 69, 95, 69, 124, -110, 40, 121, 114, -42, 95, 89, 124, -106, -15, 93, -34, 56, -26, -101, 94, 93, -123, 65, -16, 42, 10, -70, -15, 56, -16, -41, 46};
        byte[] S = {-8, -49, -75, 40, 123, 126, -113, -81, -21, -23, -3, -74, -89, -9, 23, -13, 74, -100, 94, -94, -65, -107, -32, 93, 22, -64, -105, -40, 20, 51, 31, -84, 127, 28, -125, -10, -65, 119, -43, 55, -36, -10, 97, -21, -43, -89, 79, -26, 117, -16, 15, -112, 64, -73, 121, 60, 12, -50, -126, 67, 110, 123, -64, -104};
        byte[] T = {58, -7, 88, -35, 65, 107, -50, 57, 24, -43, -1, -21, -43, 1, -2, -10, -25, 42, 102, -119, 52, 0, 106, -104, -81, 88, -67, 114, -59, 116, 17, 9, -5, 8, 57, -116, -51, -119, 102, -45, 28, -14, -44, 72, -23, 102, 95, 106, 13, -44, 55, -85, 43, -64, 26, -110, 76, -128, -124, -46, 35, 89, -117, 87};
        byte[] U = {91, -53, -86, 112, 9, 4, -106, -117, -127, 56, 92, -48, -116, -120, 64, 53, -22, 14, -99, 1, -23, -81, -13, 64, 99, -116, 63, -34, 62, -50, 104, -76, -99, 99, -54, -114, -51, 46, 65, -124, 20, 29, -123, 25, 11, -85, -89, -87, 46, 39, -64, 65, -91, 41, -18, -20, 20, 44, -113, 44, -48, 15, -70, -81};
        byte[] V = {-44, -23, -27, -76, 41, -22, 1, -126, 11, -61, 40, -67, 12, 53, -30, 63, -66, -45, -4, 117, 5, -16, 82, 52, -68, -95, 51, -80, 69, -126, -83, 120, 94, 15, -71, -14, -44, -32, 119, -21, 95, -88, -109, 46, -97, -92, 109, -33, -92, -86, 52, -102, 68, -59, -34, 47, 97, -96, 23, 59, -18, -50, 46, 50};
        byte[] W = {15, -101, 32, -1, -121, -122, 78, 78, 120, -106, -116, -2, -68, 61, -82, -102, -12, -126, 89, 95, -35, 102, -34, 98, 67, 24, -62, -103, -126, -116, -2, -19, -86, -7, -34, -16, -48, 42, 72, -18, -105, 42, -6, -11, 96, -76, 77, 2, 20, -76, -107, -17, 53, 83, 43, -32, 101, 109, 16, 66, 30, 0, -28, -94};
        byte[] X = {-23, 101, 49, 68, 108, 5, 6, 31, -16, -86, 1, 27, 54, 16, 120, 85, -74, -36, 93, 46, -34, 6, -34, 56, -73, -122, -17, 61, -49, 113, -105, 51, -36, 44, 5, 62, -71, 100, -85, -96, 80, 80, 5, 95, 43, 71, -77, 40, 110, 125, -37, -82, 121, -37, 19, -24, -44, 109, 126, 32, 110, -90, 37, 50};
        byte[] Y = {-11, -124, -98, 70, 108, 58, 120, -7, -78, 39, 39, -84, 61, 82, 45, 17, 7, -84, -6, -15, -55, 55, -12, 19, 35, 107, 75, -46, -61, 107, 100, -99, -103, -34, 110, -101, 120, 36, 17, 9, 12, -123, 63, 102, -28, -118, -19, -60, -98, 117, 22, 84, 54, 10, -5, -75, 57, 41, 50, 76, 64, -74, -59, 47};
        byte[] Z = {86, 110, 106, -23, -19, -121, 8, 6, -48, -119, -10, 9, 58, -11, -118, 85, -30, 71, 102, -97, -38, 92, 75, 48, -74, -1, 40, 110, 84, 87, 92, 112, -73, 110, -92, 26, -43, 123, 85, -27, 10, 98, 58, -99, 30, 11, -40, 73, 12, 41, -30, -31, -21, -123, -98, 59, -107, 92, 86, 48, 44, -43, -113, 73};
        Map<String, Key> byteMap = new HashMap();
        byteMap.put("a",Keys.hmacShaKeyFor(a));
        byteMap.put("b",Keys.hmacShaKeyFor(b));
        byteMap.put("c",Keys.hmacShaKeyFor(c));
        byteMap.put("d",Keys.hmacShaKeyFor(d));
        byteMap.put("e",Keys.hmacShaKeyFor(e));
        byteMap.put("f",Keys.hmacShaKeyFor(f));
        byteMap.put("g",Keys.hmacShaKeyFor(g));
        byteMap.put("h",Keys.hmacShaKeyFor(h));
        byteMap.put("i",Keys.hmacShaKeyFor(i));
        byteMap.put("j",Keys.hmacShaKeyFor(j));
        byteMap.put("k",Keys.hmacShaKeyFor(k));
        byteMap.put("l",Keys.hmacShaKeyFor(l));
        byteMap.put("m",Keys.hmacShaKeyFor(m));
        byteMap.put("n",Keys.hmacShaKeyFor(n));
        byteMap.put("o",Keys.hmacShaKeyFor(o));
        byteMap.put("p",Keys.hmacShaKeyFor(p));
        byteMap.put("q",Keys.hmacShaKeyFor(q));
        byteMap.put("r",Keys.hmacShaKeyFor(r));
        byteMap.put("s",Keys.hmacShaKeyFor(s));
        byteMap.put("t",Keys.hmacShaKeyFor(t));
        byteMap.put("u",Keys.hmacShaKeyFor(u));
        byteMap.put("v",Keys.hmacShaKeyFor(v));
        byteMap.put("w",Keys.hmacShaKeyFor(w));
        byteMap.put("x",Keys.hmacShaKeyFor(x));
        byteMap.put("y",Keys.hmacShaKeyFor(y));
        byteMap.put("z",Keys.hmacShaKeyFor(z));
        byteMap.put("A",Keys.hmacShaKeyFor(A));
        byteMap.put("B",Keys.hmacShaKeyFor(B));
        byteMap.put("C",Keys.hmacShaKeyFor(C));
        byteMap.put("D",Keys.hmacShaKeyFor(D));
        byteMap.put("E",Keys.hmacShaKeyFor(E));
        byteMap.put("F",Keys.hmacShaKeyFor(F));
        byteMap.put("G",Keys.hmacShaKeyFor(G));
        byteMap.put("H",Keys.hmacShaKeyFor(H));
        byteMap.put("I",Keys.hmacShaKeyFor(I));
        byteMap.put("J",Keys.hmacShaKeyFor(J));
        byteMap.put("K",Keys.hmacShaKeyFor(K));
        byteMap.put("L",Keys.hmacShaKeyFor(L));
        byteMap.put("M",Keys.hmacShaKeyFor(M));
        byteMap.put("N",Keys.hmacShaKeyFor(N));
        byteMap.put("O",Keys.hmacShaKeyFor(O));
        byteMap.put("P",Keys.hmacShaKeyFor(P));
        byteMap.put("Q",Keys.hmacShaKeyFor(Q));
        byteMap.put("R",Keys.hmacShaKeyFor(R));
        byteMap.put("S",Keys.hmacShaKeyFor(S));
        byteMap.put("T",Keys.hmacShaKeyFor(T));
        byteMap.put("U",Keys.hmacShaKeyFor(U));
        byteMap.put("V",Keys.hmacShaKeyFor(V));
        byteMap.put("W",Keys.hmacShaKeyFor(W));
        byteMap.put("X",Keys.hmacShaKeyFor(X));
        byteMap.put("Y",Keys.hmacShaKeyFor(Y));
        byteMap.put("Z",Keys.hmacShaKeyFor(Z));
        return byteMap.get(key);
    }

    /**
     * 获取随机Key
     * */
    public static RandomKeyResponse getRandomKey(){
        String[] keys = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        List<String> keyList = Arrays.asList(keys);
        Random random =new Random();
        int index = random.nextInt(52);
        String mapKey = keyList.get(index);
        return RandomKeyResponse.builder().mapKey(mapKey).key(getKey(mapKey)).build();
    }

}
