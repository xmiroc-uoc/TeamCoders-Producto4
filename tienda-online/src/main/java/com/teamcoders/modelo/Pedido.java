package com.teamcoders.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa un pedido realizado en la tienda online.
 *
 * <p>
 * La clase almacena la información de un pedido, incluyendo su identificador
 * automático, la fecha de realización, el cliente que lo realiza y el artículo comprado.
 * Utiliza anotaciones JPA para definir la persistencia del pedido en base de datos.
 * </p>
 *
 * <p>
 * Esta clase es clave en los procesos de facturación, seguimiento de ventas y gestión
 * de clientes. Cada pedido se relaciona directamente con un {@link Cliente} y un {@link Articulo}.
 * </p>
 *
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numeroPedido")
    private int numeroPedido;
    
    @Column(name = "unidades")
    private int unidades;
 
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_email")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "articulo_codigo")
    private Articulo articulo;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Pedido() {
    }

    /**
     * Constructor de la clase Pedido.
     * 
     * @param numeroPedido Número identificador del pedido.
     * @param unidades     Número de unidades solicitadas.
     * @param fechaPedido  Fecha y hora del pedido.
     * @param cliente      Cliente que realiza el pedido.
     * @param articulo     Artículo solicitado en el pedido.
     * @throws IllegalArgumentException si hay valores nulos o inválidos.
     */
    public Pedido(int numeroPedido, int unidades, LocalDateTime fechaPedido, Cliente cliente, Articulo articulo) {
        if (cliente == null || articulo == null || fechaPedido == null) {
            throw new IllegalArgumentException("Cliente, artículo y fecha del pedido no pueden ser nulos.");
        }
        if (unidades <= 0) {
            throw new IllegalArgumentException("El número de unidades debe ser mayor que 0.");
        }
        this.numeroPedido = numeroPedido;
        this.unidades = unidades;
        this.fechaPedido = fechaPedido;
        this.cliente = cliente;
        this.articulo = articulo;
    }

    /**
     * Devuelve el número de pedido.
     * 
     * @return Número de pedido.
     */
    public int getNumeroPedido() {
        return numeroPedido;
    }

    /**
     * Devuelve la cantidad de unidades del pedido.
     * 
     * @return Número de unidades.
     */
    public int getUnidades() {
        return unidades;
    }

    /**
     * Devuelve la fecha y hora del pedido.
     * 
     * @return Fecha del pedido.
     */
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    /**
     * Devuelve el cliente asociado al pedido.
     * 
     * @return Cliente del pedido.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Devuelve el artículo asociado al pedido.
     * 
     * @return Artículo del pedido.
     */
    public Articulo getArticulo() {
        return articulo;
    }

    /**
     * Establece el número de pedido.
     * 
     * @param numeroPedido Nuevo número de pedido.
     */
    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    /**
     * Establece la cantidad de unidades del pedido.
     * 
     * @param unidades Nueva cantidad de unidades.
     */
    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    /**
     * Establece la fecha y hora del pedido.
     * 
     * @param fechaPedido Nueva fecha del pedido.
     */
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * Establece el cliente asociado al pedido.
     * 
     * @param cliente Cliente asignado al pedido.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Establece el artículo asociado al pedido.
     * 
     * @param articulo Artículo asignado al pedido.
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    /**
     * Indica si el pedido aún puede ser cancelado.
     * 
     * @return true si no ha pasado el tiempo de preparación, false en caso
     *         contrario.
     * @throws IllegalStateException si la fecha del pedido o el artículo no están
     *                               definidos.
     */
    public boolean cancelable() {
        if (fechaPedido == null || articulo == null) {
            throw new IllegalStateException("El pedido no está completamente definido: falta fecha o artículo.");
        }
        long minutosTranscurridos = java.time.Duration.between(fechaPedido, LocalDateTime.now()).toMinutes();
        return minutosTranscurridos < articulo.getTiempoPreparacion();
    }

    /**
     * Calcula el precio total del pedido incluyendo el descuento en gastos de
     * envío.
     * 
     * @return Precio total del pedido.
     * @throws IllegalStateException si el cliente o el artículo no están definidos.
     */
    public double precioPedido() {
        if (cliente == null || articulo == null) {
            throw new IllegalStateException("El pedido no está completamente definido: falta cliente o artículo.");
        }
        double total = articulo.getPrecioVenta() * unidades;
        double descuento = cliente.descuentoEnvio();
        return total + (articulo.getGastosEnvio() * (1 - descuento));
    }

    /**
     * Devuelve una representación en forma de texto del pedido.
     * 
     * @return String con los datos del pedido.
     */
    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", unidades=" + unidades +
                ", fechaPedido=" + fechaPedido +
                ", cliente=" + cliente +
                ", articulo=" + articulo +
                '}';
    }
}
