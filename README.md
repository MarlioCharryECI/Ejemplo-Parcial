# Market Gateway API

Aplicación de gateway para consultar datos del mercado de valores con arquitectura distribuida y soporte para múltiples proveedores.

## 🏗️ Arquitectura

- **Backend**: Spring Boot (Gateway)
- **Proveedores**: Alpha Vantage API + IA Provider
- **Caché**: ConcurrentHashMap integrado
- **Autenticación**: API Keys por usuario
- **Cliente Java**: Pruebas de concurrencia

## 🚀 Endpoints API

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario
- `GET /api/auth/validate/{apiKey}` - Validar API key

### Datos de Mercado
- `GET /api/market/{provider}/{symbol}/{type}` - Obtener datos
  - `provider`: `alpha` (Alpha Vantage) o `ia` (IA Provider)
  - `symbol`: Símbolo de acción (ej: MSFT, AAPL)
  - `type`: `intra`, `daily`, `weekly`, `monthly`

### Comparación IA
- `GET /api/market/compare/{symbol1}/{symbol2}` - Comparar dos acciones

## 🔧 Configuración

1. **API Key Alpha Vantage**: Configurar en `src/main/resources/application.properties`
   ```
   alphavantage.apikey=RBD4SJ2IQSO8A614
   ```

2. **Usuarios por defecto**:
   - admin / admin@market.com
   - user1 / user1@market.com
   - user2 / user2@market.com

## 🏃‍♂️ Ejecutar Aplicación

```bash
# Iniciar servidor
mvn spring-boot:run

# Ejecutar cliente de pruebas
java -cp target/classes com.arsw.client.ConcurrentClient
java -cp target/classes com.arsw.client.AdvancedConcurrentClient
```

## 📊 Ejemplos de Uso

### Obtener datos con curl
```bash
# Login y obtener API key
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin"}'

# Usar API key para consultar datos
curl -H "X-API-Key: mk_..." \
  http://localhost:8080/api/market/alpha/MSFT/daily

# Comparar acciones con IA
curl -H "X-API-Key: mk_..." \
  http://localhost:8080/api/market/compare/MSFT/AAPL
```

## 🧪 Pruebas de Concurrencia

El cliente avanzado incluye:
- Pruebas de carga concurrente (100 requests, 20 hilos)
- Validación de endpoints
- Pruebas de comparación IA
- Métricas de rendimiento

## 🚀 Características Implementadas

✅ **Gateway Spring Boot** con múltiples proveedores  
✅ **Caché concurrente** para optimizar llamadas API  
✅ **Autenticación por API keys** (multiusuario)  
✅ **Cliente Java concurrente** con pruebas de carga  
✅ **Comparación IA** entre acciones  
✅ **CORS configurado** para frontend  
✅ **Arquitectura extensible** (fácil agregar nuevos proveedores)

---

**Nota**: Requiere Java 21+ y Maven 3.6+